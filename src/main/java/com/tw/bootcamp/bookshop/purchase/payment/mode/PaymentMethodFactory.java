package com.tw.bootcamp.bookshop.purchase.payment.mode;

import com.tw.bootcamp.bookshop.purchase.payment.PaymentMethod;
import com.tw.bootcamp.bookshop.purchase.payment.mode.Payment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;


@Component
public class PaymentMethodFactory {

    private static Map<PaymentMethod, Payment> PAYMENT_INSTANCES = new HashMap<>();

    @Autowired
    private PaymentMethodFactory(List<Payment> paymentList) {
        PAYMENT_INSTANCES = paymentList.stream()
                .collect(Collectors.toMap(Payment::type, Function.identity()));
    }

    public Payment getInstance(PaymentMethod paymentMethod) {
        return Optional.of(PAYMENT_INSTANCES.get(paymentMethod))
                .orElseThrow(() -> new UnsupportedOperationException("Unsupported payment exception"));
    }
}
