package com.ardc.ld.api;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundException extends RuntimeException {

    public NotFoundException() {
        super("entity_not_found");
    }

    public NotFoundException(String exception) {
        super(exception);
    }

}