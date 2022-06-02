package com.tw.bootcamp.bookshop.error;

public class InvalidEmailException extends Exception {
    public InvalidEmailException() {
        super("User with same email already created");
    }
}
