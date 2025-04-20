package com.check.user_check.exception.token.refreshToken;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum RefreshTokenError {

    NO_ACCESS(HttpStatus.BAD_REQUEST,
            "인증정보가 없습니다.",
            "01"
    ),
    NO_REFRESH(HttpStatus.BAD_REQUEST,
            "갱신정보가 없습니다.",
            "02"
    ),
    MALFORMED(HttpStatus.FORBIDDEN,
            "갱신 정보가 올바르지 않습니다.",
            "03"
    ),
    BAD_SIGN(HttpStatus.FORBIDDEN,
            "갱신 정보가 잘못되었습니다.",
            "04"
    ),
    EXPIRED(HttpStatus.FORBIDDEN,
            "갱신 정보가 만료되었습니다.",
            "05"
    );

    private final HttpStatus status;
    private final String message;
    private final String additionalCode;
}
