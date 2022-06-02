package com.tw.bootcamp.bookshop.purchase.payment;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.tw.bootcamp.bookshop.config.RestTemplateConfigurator;
import com.tw.bootcamp.bookshop.money.Money;
import com.tw.bootcamp.bookshop.purchase.PaymentFailedException;
import com.tw.bootcamp.bookshop.purchase.PurchaseRequest;
import com.tw.bootcamp.bookshop.purchase.payment.mode.CreditDebitCard;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.HttpStatus.*;

@SpringBootTest
class CreditDebitCardTest {

    private static WireMockServer wireMockServer;

    @Autowired
    RestTemplateConfigurator restTemplateConfigurator;
    @Autowired
    CreditDebitCard creditDebitCard;

    @BeforeAll
    static void init() {
        wireMockServer = new WireMockServer(new WireMockConfiguration().port(8090));
        wireMockServer.start();
        WireMock.configureFor("localhost", 8090);
    }

    @AfterAll
    static void clear() {
        wireMockServer.stop();
    }

    @Test
    void shouldReturnCardAsPaymentMethodType() {
        Assertions.assertEquals(PaymentMethod.CARD, creditDebitCard.type());
    }

    @Test
    void shouldPayUsingCreditCardMethod() {
        stubFor(WireMock.post(urlPathEqualTo("/payments"))
                .willReturn(aResponse()
                        .withStatus(OK.value())));
        assertDoesNotThrow(() -> creditDebitCard.pay(PurchaseRequest.builder()
                .card(Card.builder().build())
                .build(), Money.rupees(100.0)));
        verify(postRequestedFor(urlPathEqualTo("/payments"))
                .withHeader("Content-Type", equalTo("application/json")));
    }

    @Test
    void shouldThrowBadRequestExceptionWhenCardDetailAreInvalid() {
        stubFor(WireMock.post(urlPathEqualTo("/payments"))
                .willReturn(aResponse()
                        .withBody("Payment details invalid")
                        .withStatus(BAD_REQUEST.value())));

        String errorMessage = assertThrows(PaymentFailedException.class, () -> creditDebitCard.pay(PurchaseRequest.builder()
                .card(Card.builder().build())
                .build(), Money.rupees(100.0)))
                .getMessage();
        assertEquals("Payment details invalid", errorMessage);
        verify(postRequestedFor(urlPathEqualTo("/payments"))
                .withHeader("Content-Type", equalTo("application/json")));
    }

    @Test
    void shouldThrowInternalServerErrorExceptionWhenCardDetailAreInvalid() {
        stubFor(WireMock.post(urlPathEqualTo("/payments"))
                .willReturn(aResponse()
                        .withBody("Payment failed! please try again later")
                        .withStatus(INTERNAL_SERVER_ERROR.value())));

        String errorMessage = assertThrows(PaymentFailedException.class, () -> creditDebitCard.pay(PurchaseRequest.builder()
                .card(Card.builder().build())
                .build(), Money.rupees(100.0)))
                .getMessage();
        assertEquals("Payment failed! please try again later", errorMessage);
        verify(postRequestedFor(urlPathEqualTo("/payments"))
                .withHeader("Content-Type", equalTo("application/json")));
    }

}