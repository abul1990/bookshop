package com.tw.bootcamp.bookshop.order;

import com.tw.bootcamp.bookshop.book.Book;
import com.tw.bootcamp.bookshop.book.BookRepository;
import com.tw.bootcamp.bookshop.error.InvalidDateException;
import com.tw.bootcamp.bookshop.money.Money;
import com.tw.bootcamp.bookshop.purchase.payment.PaymentMethod;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.validation.ConstraintViolationException;
import java.sql.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class OrderServiceTest {
    @Autowired
    OrdersRepository ordersRepository;
    @Autowired
    OrderService orderService;
    @Autowired
    BookRepository bookRepository;
    private Book extremeProgrammingBook;

    @AfterEach
    void tearDown() {
        ordersRepository.deleteAll();
        bookRepository.delete(extremeProgrammingBook);
    }

    @BeforeEach
    void setup() {
        extremeProgrammingBook = new Book(
                0L,
                "Extreme Programming Explained",
                "Kent Beck",
                Money.rupees( 450),
                "", 1234L,
                "test",
                4.3,
                10);
        extremeProgrammingBook = bookRepository.save(extremeProgrammingBook);
    }
    @Nested
    class Orders {
        @Test
        void shouldReturnValidOrderIdWhenOrderIsSavedSuccessFully() {
            Order requestedOrder = createTestOrder();
            Order savedOrder = orderService.createOrder(requestedOrder);

            Optional<Order> order = ordersRepository.findById(savedOrder.getId());

            assertTrue(order.isPresent());
        }
        @Test
        void shouldNotCreateOrderWhenInputIsInvalid() {
            assertThrows(ConstraintViolationException.class, () -> orderService.createOrder(new Order()));
        }

    }


    @Nested
    class ViewOrders {
        @Test
        void shouldReturnListOfAllOrdersWhenDateRangeIsNotGiven() {
            Order requestedOrder = createTestOrder();
            orderService.createOrder(requestedOrder);
            List<OrderResponse> orderResponses = orderService.fetchAll();
            assertEquals(1, orderResponses.size());
        }



        @Test
        void shouldReturnEmptyListWhenOrdersAreNotPresent() {
            List<OrderResponse> orderResponses = orderService.fetchAll();
            assertEquals(0, orderResponses.size());
        }

        @Test
        void shouldReturnListOfAllOrdersWhenDateRangeIsGiven() throws InvalidDateException {
            Order requestedOrder = createTestOrder();
            requestedOrder.setOrderDate(Date.valueOf("2022-03-11"));
            orderService.createOrder(requestedOrder);
            List<OrderResponse> orderResponses = orderService.fetchOrdersByDateRange("2022-02-11","2022-05-10");
            assertEquals(1, orderResponses.size());
        }

        @Test
        void shouldReturnEmptyListWhenOrdersAreNotPresentInGivenDateRange() throws InvalidDateException {
            Order requestedOrder = createTestOrder();
            requestedOrder.setOrderDate(Date.valueOf("2022-01-11"));
            orderService.createOrder(requestedOrder);
            List<OrderResponse> orderResponses = orderService.fetchOrdersByDateRange("2022-02-11", "2022-05-10");
            assertEquals(0, orderResponses.size());
        }

        @Test
        void shouldThrowExceptionWhenStartDateIsAfterEndDate() {
            Order requestedOrder = createTestOrder();
            requestedOrder.setOrderDate(Date.valueOf("2022-03-11"));
            orderService.createOrder(requestedOrder);
            InvalidDateException exception = assertThrows(InvalidDateException.class, () -> orderService.fetchOrdersByDateRange("2022-05-11","2022-04-10"));
            assertEquals("Start Date Should be before End Date", exception.getMessage());

        }


        @Test
        void shouldThrowExceptionWhenStartDateIsAfterCurrentDate() {
            Order requestedOrder = createTestOrder();
            requestedOrder.setOrderDate(Date.valueOf("2022-03-11"));
            orderService.createOrder(requestedOrder);
            Date today = new Date(System.currentTimeMillis());
            Date tomorrow = new Date(today.getTime() + (1000 * 60 * 60 * 24));
            InvalidDateException exception = assertThrows(InvalidDateException.class, () -> orderService.fetchOrdersByDateRange(tomorrow.toString(), "2022-03-10"));
            assertEquals("Start Date and End Date should be equal or before current date", exception.getMessage());
        }

        @Test
        void shouldThrowExceptionWhenEndDateIsAfterCurrentDate() {
            Order requestedOrder = createTestOrder();
            requestedOrder.setOrderDate(Date.valueOf("2022-03-11"));
            orderService.createOrder(requestedOrder);
            Date today = new Date(System.currentTimeMillis());
            Date tomorrow = new Date(today.getTime() + (1000 * 60 * 60 * 24));
            InvalidDateException exception = assertThrows(InvalidDateException.class, () -> orderService.fetchOrdersByDateRange("2022-03-10", tomorrow.toString()));
            assertEquals("Start Date and End Date should be equal or before current date", exception.getMessage());
        }


    }

    private Order createTestOrder() {
        return new Order(
                000L,
                "Demo Customer",
                "+1-9000000000",
                new Date(System.currentTimeMillis()),
                "Demo Address Line 1",
                "Demo Address Line 2",
                "Demo City",
                "Demo State",
                "590900",
                "Demo Country",
                "demo@test.com",
                extremeProgrammingBook,
                1,
                PaymentMethod.COD,
                Money.rupees(100));
    }

}