package com.tw.bootcamp.bookshop.purchase.payment.mode;

import com.tw.bootcamp.bookshop.money.Money;
import com.tw.bootcamp.bookshop.purchase.PaymentFailedException;
import com.tw.bootcamp.bookshop.purchase.PurchaseRequest;
import com.tw.bootcamp.bookshop.purchase.payment.PaymentMethod;

public interface Payment {

    PaymentMethod type();
    void pay(PurchaseRequest purchaseRequest, Money price) throws PaymentFailedException;
}
