package com.tw.bootcamp.bookshop.purchase;

public class PaymentFailedException extends Exception {
    public PaymentFailedException(String message) {
        super(message);
    }
}
