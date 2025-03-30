package com.check.user_check.exception;

import com.check.user_check.exception.code.ServerExceptionCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public class CustomException extends RuntimeException{

    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String errorMessage;

    public CustomException(ServerExceptionCode serverExceptionCode){
        this.httpStatus = serverExceptionCode.getHttpStatus();
        this.errorCode = serverExceptionCode.getCode();
        this.errorMessage = serverExceptionCode.getMessage();
    }

}
