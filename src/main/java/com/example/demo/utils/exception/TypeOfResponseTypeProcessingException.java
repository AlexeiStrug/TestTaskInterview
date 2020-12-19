package com.example.demo.utils.exception;

public class TypeOfResponseTypeProcessingException extends RuntimeException {

    public TypeOfResponseTypeProcessingException(String msg, Throwable t) {
        super(msg, t);
    }

    public TypeOfResponseTypeProcessingException(String msg) {
        super(msg);
    }
}
