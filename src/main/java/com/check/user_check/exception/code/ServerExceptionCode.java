package com.check.user_check.exception.code;

import com.check.user_check.exception.code.dto.ErrorResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ServerExceptionCode implements BaseExceptionCode{

    BAD_REQUEST(
            HttpStatus.BAD_REQUEST,
            "잘못된 요청입니다.",
            "000201"
    ),
    DATABASE_ERROR(
            HttpStatus.INTERNAL_SERVER_ERROR,
            "데이터베이스 오류가 발생했습니다.",
            "000202"
    ),
    INTERNAL_SERVER_ERROR(
            HttpStatus.INTERNAL_SERVER_ERROR,
            "서버에 오류가 발생했습니다.",
            "000203"
    ),
    NOT_IMPLEMENTED(
            HttpStatus.NOT_IMPLEMENTED,
            "현재 지원하지 않는 기능입니다.",
            "000204"
    ),
    BAD_GATEWAY(
            HttpStatus.BAD_GATEWAY,
            "잘못된 게이트웨이 요청입니다.",
            "000205"
    ),
    SERVICE_UNAVAILABLE(
            HttpStatus.SERVICE_UNAVAILABLE,
            "서버가 과부하 상태입니다.",
            "000206"
    ),
    GATEWAY_TIMEOUT(
            HttpStatus.GATEWAY_TIMEOUT,
            "게이트웨이 응답시간이 초과되었습니다.",
            "000207"
    ),
    HTTP_VERSION_NOT_SUPPORTED(
            HttpStatus.HTTP_VERSION_NOT_SUPPORTED,
            "호환되지 않는 HTTP 버전입니다",
            "000208"
    ),
    INSUFFICIENT_STORAGE(
            HttpStatus.INSUFFICIENT_STORAGE,
            "서버에 저장공간 혹은 메모리가 부족합니다.",
            "000209"
    ),
    NOT_EXTENDED(
            HttpStatus.NOT_EXTENDED,
            "HTTP 프로토콜 전환이 되지 않았습니다.",
            "000210"
    ),
    NETWORK_AUTHENTICATION_REQUIRED(
            HttpStatus.NETWORK_AUTHENTICATION_REQUIRED,
            "네트워크 인증이 필요합니다.",
            "000211"
    );

    private final HttpStatus httpStatus;
    private final String message;
    private final String code;

    @Override
    public ErrorResponse getError() {
        return ErrorResponse.builder()
                .status(httpStatus.value())
                .message(message)
                .code(code)
                .build();
    }
}
