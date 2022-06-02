package com.tw.bootcamp.bookshop.error;

public class BookNotAvailableException extends Exception {
    public BookNotAvailableException(String message){
        super(message);
    }
    public BookNotAvailableException(){
        super();
    }
}
