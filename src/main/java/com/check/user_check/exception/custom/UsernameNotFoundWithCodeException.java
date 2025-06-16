package com.check.user_check.exception.custom;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class UsernameNotFoundWithCodeException extends UsernameNotFoundException {

    private String code;

    public UsernameNotFoundWithCodeException(String msg) {
        super(msg);
    }

    public UsernameNotFoundWithCodeException(String msg, String code) {
        super(msg);
        this.code = code;
    }

    public UsernameNotFoundWithCodeException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public String getCode() {
        return code;
    }
}
