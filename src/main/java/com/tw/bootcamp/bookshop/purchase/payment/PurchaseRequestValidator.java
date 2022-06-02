package com.tw.bootcamp.bookshop.purchase.payment;

import com.tw.bootcamp.bookshop.purchase.PurchaseRequest;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Objects;

@Service
public class PurchaseRequestValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return PurchaseRequest.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        final PurchaseRequest incomingPurchaseRequest = (PurchaseRequest) target;
        if (isValidCODRequest(incomingPurchaseRequest)) {
            errors.rejectValue("card", "InvalidPaymentMethod", "For COD payment, Card details are not mandatory");
        }
    }

    private boolean isValidCODRequest(PurchaseRequest incomingPurchaseRequest) {
        return PaymentMethod.COD.equals(incomingPurchaseRequest.getPaymentMethod()) &&
                Objects.nonNull(incomingPurchaseRequest.getCard());
    }
}
