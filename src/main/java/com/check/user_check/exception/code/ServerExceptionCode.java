package com.check.user_check.exception.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ServerExceptionCode {

    SYSTEM_ERROR("010000", "시스템 에러입니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    NOT_DATA("010001", "존재 하지 않는 데이터 입니다.", HttpStatus.INTERNAL_SERVER_ERROR);

    private final String code;
    private final String message;
    private final HttpStatus httpStatus;
}
