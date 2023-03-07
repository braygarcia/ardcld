package com.ardc.ld.api;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
public class InvalidArgumentException extends RuntimeException {
    public InvalidArgumentException() {
        super("invalid_argument");
    }

    public InvalidArgumentException(String exception) {
        super(exception);
    }
}
