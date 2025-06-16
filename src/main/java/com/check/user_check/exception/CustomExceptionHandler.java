package com.check.user_check.exception;

import com.check.user_check.dto.ResultResponse;
import com.check.user_check.exception.code.BaseExceptionCode;
import com.check.user_check.exception.code.ClientExceptionCode;
import com.check.user_check.exception.custom.CustomException;
import com.check.user_check.exception.custom.DataIntegrityViolationWithCodeException;
import com.check.user_check.exception.custom.EntityNotFoundWithCodeException;
import com.check.user_check.exception.custom.UsernameNotFoundWithCodeException;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@Log4j2
@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<Object> customException(CustomException customException){

        log.error("customException message = {}", customException.getMessage());
        log.error(customException);

        return ResultResponse.error(customException.getBaseExceptionCode());
    }

    private ResponseEntity<Object> messageWithCode(
            Exception exception, String code, BaseExceptionCode baseExceptionCode){

        String message = exception.getMessage();
        log.error("message = {}, code = {}", message, code);
        log.error(exception);

        return ResultResponse.error(baseExceptionCode, message, code);
    }

    @ExceptionHandler(UsernameNotFoundWithCodeException.class)
    private ResponseEntity<Object> UsernameNotFoundWithCodeException(UsernameNotFoundWithCodeException exception){

        return messageWithCode(exception, exception.getCode(), ClientExceptionCode.BAD_REQUEST);
    }

    @ExceptionHandler(DataIntegrityViolationWithCodeException.class)
    private ResponseEntity<Object> DataIntegrityViolationWithCodeException(DataIntegrityViolationWithCodeException exception){

        return messageWithCode(exception, exception.getCode(), ClientExceptionCode.BAD_REQUEST);
    }

    @ExceptionHandler(EntityNotFoundWithCodeException.class)
    private ResponseEntity<Object> entityNotFoundWithCodeException(EntityNotFoundWithCodeException exception){
        return messageWithCode(exception, exception.getCode(), ClientExceptionCode.BAD_REQUEST);
    }

}
