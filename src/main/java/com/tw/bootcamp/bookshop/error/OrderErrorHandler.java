package com.tw.bootcamp.bookshop.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class OrderErrorHandler {

    @ExceptionHandler(InvalidDateException.class)
    public ResponseEntity<ErrorResponse> invalidDateRangeException(
            InvalidDateException ex) {
        return new ResponseEntity<>(new ErrorResponse(ex.getMessage(),HttpStatus.BAD_REQUEST),HttpStatus.BAD_REQUEST);
    }
}
