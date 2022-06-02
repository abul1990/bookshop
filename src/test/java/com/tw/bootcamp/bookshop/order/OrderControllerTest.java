package com.tw.bootcamp.bookshop.order;

import com.tw.bootcamp.bookshop.error.InvalidDateException;
import com.tw.bootcamp.bookshop.user.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderController.class)
@WithMockUser
public class OrderControllerTest {
    @MockBean
    OrderService orderService;

    @MockBean
    UserService userService;
    @Autowired
    private MockMvc mockMvc;

    @AfterEach
    void tearDown() {
        Mockito.reset(orderService);
    }

    @Test
    void shouldListAllOrdersWhenDateRangeIsNotGiven() throws Exception {
        List<OrderResponse> orderResponses = new ArrayList<>();
        OrderResponse orderResponse = new OrderResponseTestBuilder().build();
        orderResponses.add(orderResponse);
        when(orderService.fetchAll()).thenReturn(orderResponses);

        mockMvc.perform(get("/orders").with(user("admin").password("admin")
                                .roles("USER", "ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
        verify(orderService, times(1)).fetchAll();
    }

    @Test
    void shouldListAllOrdersWhenDateRangeIsGiven() throws Exception {
        List<OrderResponse> orderResponses = new ArrayList<>();
        OrderResponse orderResponse = new OrderResponseTestBuilder().build();
        orderResponses.add(orderResponse);
        when(orderService.fetchOrdersByDateRange("2022-04-11","2022-05-10")).thenReturn(orderResponses);
        mockMvc.perform(get("/orders/startDate/2022-04-11/endDate/2022-05-10")
                        .with(user("admin").password("admin")
                                .roles("USER", "ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
        verify(orderService, times(1)).fetchOrdersByDateRange("2022-04-11", "2022-05-10");
    }

    @Test
    void shouldThrowAnExceptionWhenStartDateIsAfterEndDate() throws Exception {
        when(orderService.fetchOrdersByDateRange("2022-05-11", "2022-04-10")).thenThrow(InvalidDateException.class);
        mockMvc.perform(get("/orders/startDate/2022-05-11/endDate/2022-04-10").with(user("admin").password("admin")
                                .roles("USER", "ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

    }

    @Test
    void shouldThrowAnExceptionWhenStartDateIsAfterCurrentDate() throws Exception {
        Date today = new Date(System.currentTimeMillis());
        Date tomorrow = new Date(today.getTime() + (1000 * 60 * 60 * 24));
        when(orderService.fetchOrdersByDateRange(Mockito.any(), Mockito.any())).thenThrow(InvalidDateException.class);
        mockMvc.perform(get("/orders/startDate/" + tomorrow + "/endDate/2022-04-10").with(user("admin").password("admin")
                                .roles("USER", "ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

    }

    @Test
    void shouldThrowAnExceptionWhenEndDateIsAfterCurrentDate() throws Exception {
        Date today = new Date(System.currentTimeMillis());
        Date tomorrow = new Date(today.getTime() + (1000 * 60 * 60 * 24));
        when(orderService.fetchOrdersByDateRange(Mockito.any(), Mockito.any())).thenThrow(InvalidDateException.class);
        mockMvc.perform(get("/orders/startDate/2022-05-11/endDate/" + tomorrow).with(user("admin").password("admin")
                                .roles("USER", "ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

    }


}
