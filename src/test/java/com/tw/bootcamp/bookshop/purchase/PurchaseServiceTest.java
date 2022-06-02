package com.tw.bootcamp.bookshop.purchase;

import com.tw.bootcamp.bookshop.book.Book;
import com.tw.bootcamp.bookshop.book.BookService;
import com.tw.bootcamp.bookshop.error.BookNotAvailableException;
import com.tw.bootcamp.bookshop.money.Money;
import com.tw.bootcamp.bookshop.order.Order;
import com.tw.bootcamp.bookshop.order.OrderService;
import com.tw.bootcamp.bookshop.purchase.payment.Card;
import com.tw.bootcamp.bookshop.purchase.payment.PaymentMethod;
import com.tw.bootcamp.bookshop.purchase.payment.mode.CashOnDelivery;
import com.tw.bootcamp.bookshop.purchase.payment.mode.CreditDebitCard;
import com.tw.bootcamp.bookshop.purchase.payment.mode.PaymentMethodFactory;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PurchaseServiceTest {
    @Mock
    PaymentMethodFactory paymentMethodFactory;
    @Mock
    CashOnDelivery cashOnDelivery;
    @Mock
    CreditDebitCard creditDebitCard;
    @Mock
    private BookService bookService;
    @Mock
    private OrderService orderService;
    @InjectMocks
    private PurchaseService purchaseService;

    @Test
    void shouldPlaceAnOrderSuccessfullyWhenOrderedBookIsAvailable() throws Exception {
        Long bookId = 001L;
        PurchaseRequest purchaseRequest = new PurchaseRequest("Demo Customer", "+1-9000000000", testAddress(),
                "test", bookId, 1, PaymentMethod.COD, null);
        Order placedOrder = Mockito.mock(Order.class);

        Optional<Book> book = Optional.of(new Book(bookId, "test", "test", Money.rupees(100), "test", 1234L, "test", 4.3, 5));
        Mockito.when(bookService.fetchBookById(bookId)).thenReturn(book);
        Mockito.when(orderService.createOrder(Mockito.any())).thenReturn(
                placedOrder);
        Mockito.when(placedOrder.getId()).thenReturn(002L);
        when(paymentMethodFactory.getInstance(any())).thenReturn(cashOnDelivery);

        PurchaseResponse purchaseResponse = purchaseService.placeAnOrder(purchaseRequest);
        assertEquals(TransactionStatus.SUCCESS, purchaseResponse.getTransactionStatus());
        assertEquals(002L, purchaseResponse.getOrderId());
        verify(bookService, times(1)).updateBookTotalAvailableCount(book.get(), purchaseRequest.getNoOfCopies());
    }

    @Test
    void failToPlaceOrderWhenErrorInSavingOrderObject() throws Exception {
        Long bookId = 001L;
        PurchaseRequest purchaseRequest = new PurchaseRequest("Demo Customer", "+1-9000000000", testAddress(),
                "test", bookId, 1, PaymentMethod.COD, null);
        Order placedOrder = Mockito.mock(Order.class);

        Optional<Book> book = Optional.of(new Book(bookId, "test", "test", Money.rupees(100), "test", 1234L, "test", 4.3, 5));
        Mockito.when(bookService.fetchBookById(bookId)).thenReturn(book);
        Mockito.when(orderService.createOrder(Mockito.any())).thenReturn(
                placedOrder);
        Mockito.when(placedOrder.getId()).thenReturn(-002L);
        when(paymentMethodFactory.getInstance(any())).thenReturn(cashOnDelivery);

        PurchaseResponse purchaseResponse = purchaseService.placeAnOrder(purchaseRequest);
        assertEquals(TransactionStatus.FAILED, purchaseResponse.getTransactionStatus());
        assertEquals(000L, purchaseResponse.getOrderId());
    }

    @Test
    void throwBookNotAvailableExceptionWhenRequestedBookIsUnavailable() {
        Long bookId = 001L;
        PurchaseRequest purchaseRequest = new PurchaseRequest("Demo Customer", "+1-9000000000", testAddress(),
                "test", bookId, 1, PaymentMethod.COD, null);

        Mockito.when(bookService.fetchBookById(bookId)).thenReturn(
                Optional.empty());

        assertThrows(BookNotAvailableException.class, () -> purchaseService.placeAnOrder(purchaseRequest));
    }

    @Test
    void throwBookNotAvailableExceptionWhenRequestedBookIsAvailableButLessThanNoOfCopiesOrdered() {
        Long bookId = 001L;
        PurchaseRequest purchaseRequest = new PurchaseRequest("Demo Customer", "+1-9000000000", testAddress(),
                "test@test.com", bookId, 3, PaymentMethod.COD, null);

        Optional<Book> book = Optional.of(new Book(bookId, "test", "test", Money.rupees(100), "test", 1234L, "test", 4.3, 1));
        Mockito.when(bookService.fetchBookById(bookId)).thenReturn(book);

        assertThrows(BookNotAvailableException.class, () -> purchaseService.placeAnOrder(purchaseRequest));
    }

    @Nested
    class CardPaymentMethodTest {

        @Test
        void shouldPlaceAnOrderSuccessfullyWhenPaymentMethodIsCard() throws Exception {
            Long bookId = 001L;
            PurchaseRequest purchaseRequest = new PurchaseRequest("Demo Customer", "+1-9000000000", testAddress(),
                    "test", bookId, 1, PaymentMethod.CARD, Card.builder().build());
            Order placedOrder = Mockito.mock(Order.class);

            Optional<Book> book = Optional.of(new Book(bookId, "test", "test", Money.rupees(100), "test", 1234L, "test", 4.3, 5));
            Mockito.when(bookService.fetchBookById(bookId)).thenReturn(book);
            Mockito.when(orderService.createOrder(Mockito.any())).thenReturn(
                    placedOrder);
            Mockito.when(placedOrder.getId()).thenReturn(002L);
            when(paymentMethodFactory.getInstance(any())).thenReturn(creditDebitCard);

            PurchaseResponse purchaseResponse = purchaseService.placeAnOrder(purchaseRequest);
            assertEquals(TransactionStatus.SUCCESS, purchaseResponse.getTransactionStatus());
            assertEquals(002L, purchaseResponse.getOrderId());
            verify(bookService, times(1)).updateBookTotalAvailableCount(book.get(), purchaseRequest.getNoOfCopies());
        }

        @Test
        void throwPaymentFailedExceptionWhenCardDetailsAreInvalid() throws ParseException, PaymentFailedException {
            Long bookId = 001L;

            PurchaseRequest purchaseRequest = new PurchaseRequest("Demo Customer", "+1-9000000000", testAddress(),
                    "test", bookId, 1, PaymentMethod.CARD, Card.builder().build());
            Order placedOrder = Mockito.mock(Order.class);

            Optional<Book> book = Optional.of(new Book(bookId, "test", "test", Money.rupees(100), "test", 1234L, "test", 4.3, 5));
            Mockito.when(bookService.fetchBookById(bookId)).thenReturn(book);
            when(paymentMethodFactory.getInstance(PaymentMethod.CARD)).thenReturn(creditDebitCard);
            doThrow(new PaymentFailedException("Internal server error")).when(creditDebitCard).pay(any(PurchaseRequest.class), any(Money.class));

            assertThrows(PaymentFailedException.class, () -> purchaseService.placeAnOrder(purchaseRequest));
        }

    }

    private Card mockCard() throws ParseException {
        return new Card("1234-1234-1243-1234", "123", new SimpleDateFormat("MM/yyyy").parse("02/2023"));
    }

    private OrderAddressDTO testAddress() {
        return new OrderAddressDTO(
                        "test",
                        "test",
                        "test",
                        "test",
                        "test",
                        "test");
    }
}