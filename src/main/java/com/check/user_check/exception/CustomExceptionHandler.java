package com.check.user_check.exception;

import com.check.user_check.dto.ResultResponse;
import com.check.user_check.exception.code.ClientExceptionCode;
import com.check.user_check.exception.custom.CustomException;
import lombok.extern.log4j.Log4j2;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@Log4j2
@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<Object> customException(CustomException customException){

        log.error("커스텀 에러 - ", customException.getErrorMessage());

        return ResponseEntity.status(customException.getHttpStatus()).body(
                Map.of(
                        "message", customException.getErrorMessage(),
                        "code", customException.getErrorCode()
                )
        );
    }

    private ResponseEntity<Object> messageWithCode(Exception exception){
        String code = exception.getMessage().contains("|") ?
                exception.getMessage().split("\\|")[1] : null;

        String message = exception.getMessage().contains("|") ?
                exception.getMessage().split("\\|")[0] : exception.getMessage();

        return ResultResponse.error(ClientExceptionCode.BAD_REQUEST, message, code);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    private ResponseEntity<Object> usernameNotFoundException(UsernameNotFoundException exception){

        return messageWithCode(exception);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    private ResponseEntity<Object> dataIntegrityViolationException(DataIntegrityViolationException exception){

        return messageWithCode(exception);
    }

}
