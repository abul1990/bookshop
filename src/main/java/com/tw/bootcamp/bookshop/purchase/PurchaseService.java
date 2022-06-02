package com.tw.bootcamp.bookshop.purchase;

import com.tw.bootcamp.bookshop.book.Book;
import com.tw.bootcamp.bookshop.book.BookService;
import com.tw.bootcamp.bookshop.purchase.payment.mode.PaymentMethodFactory;
import com.tw.bootcamp.bookshop.error.BookNotAvailableException;
import com.tw.bootcamp.bookshop.order.Order;
import com.tw.bootcamp.bookshop.order.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class PurchaseService {

    @Value("${shipping.charges:100}")
    private double shippingCharges;

    @Autowired
    private BookService bookService;

    @Autowired
    private OrderService orderService;

    @Autowired
    PaymentMethodFactory paymentMethodFactory;

    public PurchaseResponse placeAnOrder(PurchaseRequest purchaseRequest) throws Exception {
        Book book = getBookIfAvailable(purchaseRequest);
        Order requestedOrder = Order.create(purchaseRequest, book);
        requestedOrder.addShippingCharges(shippingCharges);
        paymentMethodFactory.getInstance(purchaseRequest.getPaymentMethod())
                .pay(purchaseRequest, requestedOrder.getTotalPrice());
        Order placedOrder = orderService.createOrder(requestedOrder);
        if (!ifOrderPlaced(placedOrder)) {
            return new PurchaseResponse(000L, TransactionStatus.FAILED);
        }
        bookService.updateBookTotalAvailableCount(book, purchaseRequest.getNoOfCopies());
        return new PurchaseResponse(placedOrder.getId(), TransactionStatus.SUCCESS);
    }

    private Book getBookIfAvailable(PurchaseRequest purchaseRequest) throws BookNotAvailableException {
        Optional<Book> book = bookService.fetchBookById(purchaseRequest.getBookId());
        if (!book.isPresent()) {
            throw new BookNotAvailableException("Book not found");
        }
        if (book.get().getQuantity() == 0) {
            throw new BookNotAvailableException("Book is out of stock");
        }
        if (book.get().getQuantity() < purchaseRequest.getNoOfCopies()) {
            throw new BookNotAvailableException("Only " + book.get().getQuantity() + " copies are available");
        }
        return book.get();
    }

    private boolean ifOrderPlaced(Order placedOrder) {
        return null != placedOrder && placedOrder.getId() > 0;
    }
}
