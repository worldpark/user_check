package com.check.user_check.exception;

import com.check.user_check.exception.code.ServerExceptionCode;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Log4j2
@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<CustomErrorResponse> customException(CustomException customException){

        log.error("CustomException ErrorCode = {}, Message = {}"
                , customException.getErrorCode()
                , customException.getErrorMessage());

        CustomErrorResponse customErrorResponse = CustomErrorResponse.builder()
                .errorCode(customException.getErrorCode())
                .errorMessage(customException.getErrorMessage())
                .build();

        return new ResponseEntity<>(customErrorResponse, customException.getHttpStatus());
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<CustomErrorResponse> userNameNotFoundException(
            UsernameNotFoundException usernameNotFoundException){

        String message = usernameNotFoundException.getMessage();
        message = !message.equals("") ? message : "User not Found";

        log.error(message, usernameNotFoundException);

        CustomErrorResponse customErrorResponse = CustomErrorResponse.builder()
                .errorCode(ServerExceptionCode.NOT_DATA.getCode())
                .errorMessage(message)
                .build();

        return new ResponseEntity<>(customErrorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MalformedJwtException.class)
    public ResponseEntity<CustomErrorResponse> malformedJwtException(MalformedJwtException malformedJwtException){

        String message = malformedJwtException.getMessage();
        message = !message.equals("") ? message : "JWT 토큰이 유효하지 않습니다.";

        log.error(message, malformedJwtException);

        CustomErrorResponse customErrorResponse = CustomErrorResponse.builder()
                .errorCode("000001")
                .errorMessage(message)
                .build();

        return new ResponseEntity<>(customErrorResponse, HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler(UnsupportedJwtException.class)
    public ResponseEntity<CustomErrorResponse> malformedJwtException(UnsupportedJwtException unsupportedJwtException){

        String message = unsupportedJwtException.getMessage();
        message = !message.equals("") ? message : "지원 하지 않는 JWT 토큰 입니다.";

        log.error(message, unsupportedJwtException);

        CustomErrorResponse customErrorResponse = CustomErrorResponse.builder()
                .errorCode("000001")
                .errorMessage("JWT 토큰이 유효하지 않습니다.")
                .build();

        return new ResponseEntity<>(customErrorResponse, HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<CustomErrorResponse> malformedJwtException(IllegalArgumentException illegalArgumentException){

        String message = illegalArgumentException.getMessage();
        message = !message.equals("") ? message : "JWT claims가 비어 있습니다.";

        log.error(message, illegalArgumentException);

        CustomErrorResponse customErrorResponse = CustomErrorResponse.builder()
                .errorCode("000001")
                .errorMessage("JWT 토큰이 유효하지 않습니다.")
                .build();

        return new ResponseEntity<>(customErrorResponse, HttpStatus.BAD_REQUEST);

    }

}
