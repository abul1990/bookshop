package com.tw.bootcamp.bookshop.purchase;

import com.tw.bootcamp.bookshop.purchase.payment.Card;
import com.tw.bootcamp.bookshop.purchase.payment.PaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import javax.validation.Valid;
import javax.validation.constraints.*;

@Builder
@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class PurchaseRequest {
    @NotBlank(message = "Customer name can not be empty")
    private String customerName;

    @NotBlank(message = "Customer mobile number can not be empty")
    @Pattern(regexp="\\d{10}|(?:\\d{3}-){2}\\d{4}|\\(\\d{3}\\)\\d{3}-?\\d{4}",message = "Please provide a valid mobile number")
    private String mobileNumber;

    @Valid
    @NotNull(message = "Delivery address can not be null")
    private OrderAddressDTO deliveryAddress;

    @Email(message = "Email is not valid", regexp = "^\\w+([\\.-]?\\w+)*@\\w+([\\.-]?\\w+)*(\\.\\w{2,3})+$")
    @NotBlank(message = "Email cannot be empty")
    private String userEmailId;

    @NotNull(message = "Book id can not be empty")
    private Long bookId;

    @Min(value = 1,message = "Number of ordered copies should be greater than zero")
    @NotNull(message = "number of ordered copies should not be null")
    private Integer noOfCopies;

    @Builder.Default
    @NotNull(message = "Payment method can not be null")
    private PaymentMethod paymentMethod = PaymentMethod.COD;

    @Valid
    private Card card;
}
