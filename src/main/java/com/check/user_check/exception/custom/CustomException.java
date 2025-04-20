package com.check.user_check.exception.custom;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CustomException extends RuntimeException{

    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String errorMessage;

    public CustomException(HttpStatus httpStatus, String errorCode, String errorMessage){
        super("CustomException");
        this.httpStatus = httpStatus;
        this.errorMessage = errorMessage;
        this.errorCode = errorCode;
    }

}
