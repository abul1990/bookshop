package com.tw.bootcamp.bookshop.purchase.payment.mode;

import com.tw.bootcamp.bookshop.money.Money;
import com.tw.bootcamp.bookshop.purchase.PurchaseRequest;
import com.tw.bootcamp.bookshop.purchase.payment.PaymentMethod;
import com.tw.bootcamp.bookshop.purchase.payment.mode.Payment;
import org.springframework.stereotype.Component;

@Component
public class CashOnDelivery implements Payment {

    @Override
    public PaymentMethod type() {
        return PaymentMethod.COD;
    }

    @Override
    public void pay(PurchaseRequest purchaseRequest, Money price) {
        //accept COD payment
    }
}
