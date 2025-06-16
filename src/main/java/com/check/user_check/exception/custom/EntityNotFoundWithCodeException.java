package com.check.user_check.exception.custom;

import jakarta.persistence.EntityNotFoundException;

public class EntityNotFoundWithCodeException extends EntityNotFoundException {

    private String code;

    public EntityNotFoundWithCodeException() {
        super();
    }

    public EntityNotFoundWithCodeException(String message) {
        super(message);
    }

    public EntityNotFoundWithCodeException(String message, String code) {
        super(message);
        this.code = code;
    }

    public EntityNotFoundWithCodeException(Exception cause) {
        super(cause);
    }

    public EntityNotFoundWithCodeException(String message, Exception cause) {
        super(message, cause);
    }

    public String getCode() {
        return code;
    }
}
