package com.tw.bootcamp.bookshop.purchase;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tw.bootcamp.bookshop.purchase.payment.PaymentMethod;
import com.tw.bootcamp.bookshop.purchase.payment.PurchaseRequestValidator;
import com.tw.bootcamp.bookshop.user.UserService;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PurchaseController.class)
public class PurchaseControllerTest {
    @MockBean
    UserService userService;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private PurchaseService purchaseService;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PurchaseRequestValidator purchaseRequestValidator;

    @Test
    void shouldReturnSuccessOnOrderingOneBook() throws Exception {


        PurchaseRequest purchaseRequest = new PurchaseRequest("Demo Customer", "9000000000", testAddress(), "admin@test.com", 1L, 1, PaymentMethod.COD, null);

        PurchaseResponse purchaseResponse = new PurchaseResponse(1L, TransactionStatus.SUCCESS);
        when(purchaseService.placeAnOrder(Mockito.any())).thenReturn(purchaseResponse);

        mockMvc.perform(post("/user/order").with(user("admin").password("admin").roles("USER", "ADMIN")).content(objectMapper.writeValueAsString(purchaseRequest)).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andExpect(content().string(objectMapper.writeValueAsString(purchaseResponse)));

        verify(purchaseService, times(1)).placeAnOrder(Mockito.any());
    }

    @Test
    void shouldThrowBadRequestExceptionWhenThereIsAnyMissingValueInRequest() throws Exception {

        PurchaseRequest purchaseRequest = new PurchaseRequest("Demo Customer", "+1-9000000000", testAddress(), "", 1L, 1, PaymentMethod.COD, null);


        mockMvc.perform(post("/user/order").with(user("admin").password("admin").roles("USER", "ADMIN")).content(objectMapper.writeValueAsString(purchaseRequest)).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());

    }
    @Test
    void shouldThrowBadRequestExceptionWhenThereAnyInvalidPaymentMethodIsSent() throws Exception {

        JSONObject purchaseRequest = new JSONObject("{\n" +
                "    \"customerName\": \"Demo Customer 2\",\n" +
                "    \"mobileNumber\": \"9000000010\",\n" +
                "    \"deliveryAddress\": {\n" +
                "        \"lineNoOne\": \"Demo Line no 1\",\n" +
                "        \"lineNoTwo\": \"Demo Line no 2\",\n" +
                "        \"city\": \"Hyderabad\",\n" +
                "        \"state\": \"Telangana\",\n" +
                "        \"pinCode\": \"500000\",\n" +
                "        \"country\": \"India\"\n" +
                "    },\n" +
                "    \"userEmailId\": \"demo@test.com\",\n" +
                "    \"bookId\": 6,\n" +
                "    \"noOfCopies\": 100,\n" +
                "    \"paymentMethod\": \"xxx\"\n" +
                "}");

        mockMvc.perform(post("/user/order")
                        .with(user("admin")
                                .password("admin")
                                .roles("USER", "ADMIN"))
                        .content(purchaseRequest.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0].message").value("xxx is an invalid PaymentMethod"));
    }
    private OrderAddressDTO testAddress() {
        return new OrderAddressDTO("test", "test", "test", "test", "900123", "test");
    }
}
