package com.tw.bootcamp.bookshop.purchase.payment;

import com.tw.bootcamp.bookshop.money.Money;
import com.tw.bootcamp.bookshop.purchase.PurchaseRequest;
import com.tw.bootcamp.bookshop.purchase.payment.mode.CashOnDelivery;
import com.tw.bootcamp.bookshop.purchase.payment.mode.Payment;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CashOnDeliveryTest {

    @Test
    void shouldReturnCODAsPaymentMethodType() {
        Payment cashOnDelivery = new CashOnDelivery();
        Assertions.assertEquals(PaymentMethod.COD, cashOnDelivery.type());
    }

    @Test
    void shouldPayUsingCashOnDeliveryMethod() {
        Payment cashOnDelivery = new CashOnDelivery();
        assertDoesNotThrow(() -> cashOnDelivery.pay(PurchaseRequest.builder().build(), Money.rupees(100.0)));
    }
}
