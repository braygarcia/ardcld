package com.ardc.ld.api;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class EntityAlreadyExistsException extends RuntimeException {
    public EntityAlreadyExistsException() {
        super("entity_already_exists");
    }

    public EntityAlreadyExistsException(String exception) {
        super(exception);
    }
}
