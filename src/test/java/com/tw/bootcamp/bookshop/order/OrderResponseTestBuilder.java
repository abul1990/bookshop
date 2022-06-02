package com.tw.bootcamp.bookshop.order;

import com.tw.bootcamp.bookshop.money.Money;
import com.tw.bootcamp.bookshop.purchase.OrderAddressDTO;
import com.tw.bootcamp.bookshop.user.address.Address;

import java.sql.Date;

public class OrderResponseTestBuilder {

    private final OrderResponse.OrderResponseBuilder orderResponseBuilder;
    public OrderResponseTestBuilder() {

        orderResponseBuilder = OrderResponse.builder()
                .id(123L)
                .bookId(123L)
                .noOfCopies(1)
                .totalPrice(Money.rupees(300))
                .orderDate( Date.valueOf("2022-03-11"))
                .address(createAddress())
                .bookISBN(123L)
                .bookName("Harry Potter")
                .customerName("TestUser")
                .customerPhoneNumber("9999999999");



    }

    private OrderAddressDTO createAddress() {
        OrderAddressDTO address = new OrderAddressDTO();
        address.setLineNoOne("TestData");
        address.setLineNoOne("TestData");
        address.setCity("TestData");
        address.setCountry("TestData");
        address.setState("TestData");
        address.setPinCode("TestData");
        return address;
    }

    public OrderResponse build() {
        return orderResponseBuilder.build();
    }
}
