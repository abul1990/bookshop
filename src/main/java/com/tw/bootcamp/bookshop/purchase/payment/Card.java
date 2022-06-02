package com.tw.bootcamp.bookshop.purchase.payment;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Value;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Date;


@Value
@Builder
@AllArgsConstructor
public class Card {

    @Pattern(regexp = "^(?:\\d{4}-){3}\\d{4}|\\d{16}$", message = "Card number is not correct")
    @NotNull(message = "Please provide a card number")
    String number;

    @Pattern(regexp = "^\\d{3}$", message = "CVV is not correct")
    @NotNull(message = "Please provide a cvv")
    String cvv;

    @JsonFormat(pattern = "MM/yyyy", shape = JsonFormat.Shape.STRING)
    @Future(message = "Credit card is expired")
    @NotNull(message = "Please provide a expiry")
    Date expiryDate;

}
