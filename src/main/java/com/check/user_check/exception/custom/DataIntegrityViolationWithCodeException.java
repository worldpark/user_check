package com.check.user_check.exception.custom;

import org.springframework.dao.DataIntegrityViolationException;

public class DataIntegrityViolationWithCodeException extends DataIntegrityViolationException {

    private String code;

    public DataIntegrityViolationWithCodeException(String msg) {
        super(msg);
    }

    public DataIntegrityViolationWithCodeException(String msg, String code) {
        super(msg);
        this.code = code;
    }

    public DataIntegrityViolationWithCodeException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public String getCode() {
        return code;
    }
}
