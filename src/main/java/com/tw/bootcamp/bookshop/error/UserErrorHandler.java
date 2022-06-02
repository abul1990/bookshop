package com.tw.bootcamp.bookshop.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class UserErrorHandler {
    @ExceptionHandler({ InvalidEmailException.class })
    public ResponseEntity<ErrorResponse> handleCreateUserError(Exception ex) {
        ErrorResponse apiError = new ErrorResponse(ex.getMessage(),HttpStatus.UNPROCESSABLE_ENTITY);
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }
}
