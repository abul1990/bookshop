package com.tw.bootcamp.bookshop.order;

import com.tw.bootcamp.bookshop.error.InvalidDateException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/orders")
    @Operation(summary = "List all orders", description = "Lists all orders in bookshop", tags = {"Order Service"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List all Orders",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = OrderResponse.class))})
    })
    List<OrderResponse> viewOrders() {
        return orderService.fetchAll();
    }


    @GetMapping(path="/orders/startDate/{startDate}/endDate/{endDate}",produces=MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "List orders within given date range", description = "Lists orders within given date range in bookshop", tags = {"Order Service"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List orders within given date range",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = OrderResponse.class))})
    })
    ResponseEntity viewOrdersByDateRange(@PathVariable("startDate") String startDate, @PathVariable("endDate") String endDate) throws InvalidDateException {

            List<OrderResponse> orderResponses = orderService.fetchOrdersByDateRange(startDate, endDate);
            return new ResponseEntity<>(orderResponses, HttpStatus.OK);
    }

}
