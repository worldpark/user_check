package com.check.user_check.exception;

import com.check.user_check.dto.ResultResponse;
import com.check.user_check.exception.code.BaseExceptionCode;
import com.check.user_check.exception.code.ClientExceptionCode;
import com.check.user_check.exception.custom.CustomException;
import com.check.user_check.exception.custom.DataIntegrityViolationWithCodeException;
import com.check.user_check.exception.custom.EntityNotFoundWithCodeException;
import com.check.user_check.exception.custom.UsernameNotFoundWithCodeException;
import jakarta.persistence.OptimisticLockException;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import java.util.Objects;


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

    @ExceptionHandler({
            ObjectOptimisticLockingFailureException.class,
            OptimisticLockException.class
    })
    private ResponseEntity<Object> optimisticLockingFailureException(Exception exception){

        return messageWithCode(
                exception,
                "050303",
                ClientExceptionCode.CONFLICT);
    }

    @ExceptionHandler({
            MethodArgumentNotValidException.class,
            BindException.class
    })
    private ResponseEntity<Object> validationException(Exception exception){
        String field;
        String message;

        if (exception instanceof MethodArgumentNotValidException methodArgumentNotValidException) {
            field = Objects.requireNonNull(methodArgumentNotValidException.getBindingResult().getFieldError()).getField();
            message = methodArgumentNotValidException.getBindingResult().getFieldError().getDefaultMessage();
        } else {
            BindException bindException = (BindException) exception;
            field = Objects.requireNonNull(bindException.getBindingResult().getFieldError()).getField();
            message = bindException.getBindingResult().getFieldError().getDefaultMessage();
        }

        return ResultResponse.validation(
                ClientExceptionCode.BAD_REQUEST,
                field,
                message,
                ClientExceptionCode.BAD_REQUEST.getCode()
        );
    }

    @ExceptionHandler(HandlerMethodValidationException.class)
    private ResponseEntity<Object> handlerMethodValidationException(HandlerMethodValidationException exception){
        String message = exception.getAllErrors().stream()
                .map(error -> error.getDefaultMessage())
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(ClientExceptionCode.BAD_REQUEST.getMessage());

        String field = exception.getParameterValidationResults().stream()
                .filter(result -> result.getMethodParameter() != null)
                .map(result -> result.getMethodParameter().getParameterName())
                .filter(Objects::nonNull)
                .findFirst()
                .orElse("request");

        return ResultResponse.validation(
                ClientExceptionCode.BAD_REQUEST,
                field,
                message,
                ClientExceptionCode.BAD_REQUEST.getCode()
        );
    }

}
