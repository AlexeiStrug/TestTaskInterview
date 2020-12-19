package com.example.demo.utils.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class ForbiddenAccessToTheSystem extends RuntimeException {

    public ForbiddenAccessToTheSystem(String message) {
        super(message);
    }
}
