package com.tw.bootcamp.bookshop.purchase.payment.mode;

import com.tw.bootcamp.bookshop.config.RestTemplateConfigurator;
import com.tw.bootcamp.bookshop.money.Money;
import com.tw.bootcamp.bookshop.purchase.PaymentFailedException;
import com.tw.bootcamp.bookshop.purchase.PurchaseRequest;
import com.tw.bootcamp.bookshop.purchase.payment.Card;
import com.tw.bootcamp.bookshop.purchase.payment.CardPaymentRequest;
import com.tw.bootcamp.bookshop.purchase.payment.PaymentMethod;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@Component
public class CreditDebitCard implements Payment {


    private final String externalPaymentApiUrl;

    RestTemplate restTemplate;

    public CreditDebitCard(@Value("${external.payment.api.url}") String url) {
        this.externalPaymentApiUrl = url;
        this.restTemplate = RestTemplateConfigurator.getRestTemplate();
    }

    @Override
    public PaymentMethod type() {
        return PaymentMethod.CARD;
    }

    @Override
    public void pay(PurchaseRequest purchaseRequest, Money price) throws PaymentFailedException {

        try {
            HttpEntity<CardPaymentRequest> entity = new HttpEntity<CardPaymentRequest>(buildCardPaymentRequest(purchaseRequest, price), buildHttpHeaders());
            restTemplate.exchange(externalPaymentApiUrl, HttpMethod.POST, entity, String.class)
                    .getStatusCode();
        } catch (RestClientResponseException ex) {
            int paymentStatus = ex.getRawStatusCode();

            if(paymentStatus == HttpStatus.BAD_REQUEST.value()){
                throw new PaymentFailedException("Payment details invalid");
            }

          throw new PaymentFailedException("Payment failed! please try again later");
        }
    }

    private HttpHeaders buildHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        return headers;
    }

    public CardPaymentRequest buildCardPaymentRequest(PurchaseRequest purchaseRequest, Money price) throws PaymentFailedException {
        Card card = purchaseRequest.getCard();
        if(card == null){
            throw new PaymentFailedException("Card details are missing");
        }

        return CardPaymentRequest.createCardPayment(card, price.getAmount());
    }
}
