package com.tw.bootcamp.bookshop.order;

import com.tw.bootcamp.bookshop.book.Book;
import com.tw.bootcamp.bookshop.money.Money;
import com.tw.bootcamp.bookshop.purchase.OrderAddressDTO;
import com.tw.bootcamp.bookshop.purchase.PurchaseRequest;
import com.tw.bootcamp.bookshop.purchase.payment.PaymentMethod;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.sql.Date;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String customerName;

    @NotBlank
    private String mobileNumber;

    private Date orderDate;

    @NotBlank
    private String addressLineNoOne;

    private String addressLineNoTwo;

    @NotBlank
    private String addressCity;

    @NotBlank
    private String addressState;

    @NotBlank
    private String addressPinCode;

    @NotBlank
    private String addressCountry;

    @NotBlank(message = "Email is mandatory")
    private String userEmailId;

    @NotNull(message = "Book id can not be null")

    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    private int noOfCopies;

    @Column(columnDefinition = "VARCHAR")
    private PaymentMethod paymentMethod;

    @Embedded
    private Money totalPrice;

    public static Order create(PurchaseRequest purchaseRequest, Book book) {
        OrderAddressDTO address = purchaseRequest.getDeliveryAddress();
        return new Order(
                00L,
                purchaseRequest.getCustomerName(),
                purchaseRequest.getMobileNumber(),
                new Date(System.currentTimeMillis()),
                address.getLineNoOne(),
                address.getLineNoTwo(),
                address.getCity(),
                address.getState(),
                address.getPinCode(),
                address.getCountry(),
                purchaseRequest.getUserEmailId(),
                book,
                purchaseRequest.getNoOfCopies(),
                purchaseRequest.getPaymentMethod(),
                getTotalPrice(book, purchaseRequest));
    }

    private static Money getTotalPrice(Book book, PurchaseRequest purchaseRequest) {
        return Money.rupees(book.getPrice()
                .getAmount() * purchaseRequest.getNoOfCopies());
    }

    public void addShippingCharges(double shippingCharges) {
        this.totalPrice = Money.rupees(this.getTotalPrice()
                .getAmount() + shippingCharges);
    }


}
