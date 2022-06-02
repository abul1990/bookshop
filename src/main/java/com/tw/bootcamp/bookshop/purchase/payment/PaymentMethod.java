package com.tw.bootcamp.bookshop.purchase.payment;

import com.fasterxml.jackson.annotation.JsonCreator;
import org.apache.commons.lang3.StringUtils;

public enum PaymentMethod {
    COD("COD"),
    CARD("CARD");

    private final String type;
    PaymentMethod(String type) {
        this.type = type;
    }

    String getType() {
        return type;
    }

    @JsonCreator
    public static PaymentMethod create(String value) throws IllegalArgumentException {
        if(StringUtils.isBlank(value)) {
            throw new IllegalArgumentException("PaymentMethod can not be empty or null");
        }
        for(PaymentMethod v : values()) {
            if(value.equals(v.getType())) {
                return v;
            }
        }
        throw new IllegalArgumentException(value + " is an invalid PaymentMethod");
    }
}
