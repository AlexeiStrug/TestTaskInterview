package com.example.demo.utils.exception;

public class TypeOfRequestProcessingException extends Exception {

    public TypeOfRequestProcessingException(String msg, Throwable t) {
        super(msg, t);
    }

    public TypeOfRequestProcessingException(String msg) {
        super(msg);
    }
}
