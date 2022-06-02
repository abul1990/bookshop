package com.tw.bootcamp.bookshop.error;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
public class ValidationErrorHandler {
    @ExceptionHandler({ ConstraintViolationException.class })
    public ResponseEntity<ErrorResponse> handleConstraintViolation(
            ConstraintViolationException ex) {
        List<Error> errors = new ArrayList<>();
        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            errors.add(new Error(violation.getPropertyPath().toString(),violation.getMessage()));
        }
        ErrorResponse apiError =
                new ErrorResponse(HttpStatus.UNPROCESSABLE_ENTITY,"Validation failed", errors);
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> processValidationIllegalError(HttpMessageNotReadableException ex) {
        IllegalArgumentException exception = (IllegalArgumentException) ex.getMostSpecificCause();
        return new ResponseEntity<>(new ErrorResponse(exception.getMessage(),HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException ex) {
        List<Error> errors = new ArrayList<>();
        BindingResult result = ex.getBindingResult();
        List<FieldError> fieldErrors = result.getFieldErrors();
        for(FieldError fieldError:fieldErrors){
            errors.add(new Error(fieldError.getField(),fieldError.getDefaultMessage()));
        }
        ErrorResponse errorResponse = new ErrorResponse(errors,HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}
