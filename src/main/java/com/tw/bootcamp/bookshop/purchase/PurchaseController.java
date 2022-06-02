package com.tw.bootcamp.bookshop.purchase;

import com.tw.bootcamp.bookshop.error.Error;
import com.tw.bootcamp.bookshop.error.ErrorResponse;
import com.tw.bootcamp.bookshop.purchase.payment.PurchaseRequestValidator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class PurchaseController {

    @Autowired
    PurchaseService purchaseService;

    @Autowired
    PurchaseRequestValidator purchaseRequestValidator;

    @PostMapping(path = "/user/order", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Placing order for a book", description = "Placing order for a book", tags = {"Purchase Service"})
    @ApiResponses(value = {@ApiResponse(responseCode = "200",
            description = "Order placed", content = {@Content(mediaType = "application/json",
            schema = @Schema(implementation = ResponseEntity.class))})})
    public ResponseEntity<Object> placeAnOrder(@RequestBody @Valid PurchaseRequest purchaseRequest, BindingResult bindingResult) throws Exception {
        purchaseRequestValidator.validate(purchaseRequest, bindingResult);
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(new ErrorResponse(collectErrors(bindingResult), HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
        }
        PurchaseResponse purchaseResponse = purchaseService.placeAnOrder(purchaseRequest);
        return new ResponseEntity<>(purchaseResponse, HttpStatus.OK);
    }

    private List<Error> collectErrors(BindingResult bindingResult) {
        return bindingResult.getAllErrors().stream().map(objectError ->
                        new Error(((FieldError) objectError).getField(), objectError.getDefaultMessage()))
                .collect(Collectors.toList());
    }
}
