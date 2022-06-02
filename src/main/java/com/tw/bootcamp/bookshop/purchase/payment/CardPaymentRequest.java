package com.tw.bootcamp.bookshop.purchase.payment;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import java.util.Date;

@Value
@Builder
@AllArgsConstructor
public class CardPaymentRequest {


    Double amount;

    @JsonProperty("creditCardNumber")
    String number;

    @JsonProperty("cardSecurityCode")
    String cvv;

    @JsonFormat(pattern = "MM/yyyy", shape = JsonFormat.Shape.STRING)
    @JsonProperty("creditCardExpiration")
    Date expiryDate;

    public static CardPaymentRequest createCardPayment(Card card, Double amount) {
        return CardPaymentRequest.builder().number(card.getNumber()).cvv(card.getCvv()).expiryDate(card.getExpiryDate()).amount(amount).build();

    }
}
