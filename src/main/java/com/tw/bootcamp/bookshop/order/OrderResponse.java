package com.tw.bootcamp.bookshop.order;

import com.tw.bootcamp.bookshop.money.Money;
import com.tw.bootcamp.bookshop.purchase.OrderAddressDTO;
import com.tw.bootcamp.bookshop.user.address.Address;
import lombok.*;

import java.util.Date;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponse {
    private Long id;
    private OrderAddressDTO address;
    private Long bookId;
    private Integer noOfCopies;
    private Money totalPrice;
    private String bookName;
    private Long bookISBN;
    private String customerName;
    private String customerPhoneNumber;
    private Date orderDate;
}
