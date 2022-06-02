package com.tw.bootcamp.bookshop.error;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {
    private HttpStatus status;
    private String message;
    private List<Error> errors = new ArrayList<>();

    public ErrorResponse(String message) {
        this.errors.add(new Error(message));
        this.status = HttpStatus.INTERNAL_SERVER_ERROR;
    }

    public ErrorResponse(String message, HttpStatus status) {
        this.errors.add(new Error(message));
        this.status = status;
    }

    public ErrorResponse(String name,String message, String type, HttpStatus status) {
        this.errors.add(new Error(name,message, type));
        this.status = status;

    }

    public ErrorResponse(List<Error> error, HttpStatus status) {
        this.errors.addAll(error);
        this.status = status;

    }

    public ErrorResponse(List<Error> error) {
        this.errors.addAll(error);
        this.status = HttpStatus.INTERNAL_SERVER_ERROR;

    }
}
