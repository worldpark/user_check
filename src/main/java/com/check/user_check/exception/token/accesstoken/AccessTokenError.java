package com.check.user_check.exception.token.accesstoken;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AccessTokenError {

    UNACCEPTABLE(
            HttpStatus.UNAUTHORIZED,
            "접근 권한이 없습니다.",
            "01"
    ),
    BAD_TYPE(HttpStatus.UNAUTHORIZED,
            "인증 타입이 잘못되었습니다.",
            "02"
    ),
    BAD_SIGN(HttpStatus.FORBIDDEN,
            "인증 정보가 잘못되었습니다.",
            "03"
    ),
    MALFORMED(HttpStatus.FORBIDDEN,
            "인증 정보가 올바르지 않습니다.",
            "04"
    ),
    EXPIRED(HttpStatus.FORBIDDEN,
            "인증이 만료되었습니다.",
            "05"
    );

    private final HttpStatus status;
    private final String message;
    private final String additionalCode;
}
