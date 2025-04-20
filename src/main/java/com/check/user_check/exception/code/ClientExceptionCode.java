package com.check.user_check.exception.code;

import com.check.user_check.exception.code.dto.ErrorResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ClientExceptionCode implements BaseExceptionCode{

    BAD_REQUEST(
            HttpStatus.BAD_REQUEST,
            "잘못된 요청입니다.",
            "000101"
    ),
    UNAUTHORIZED(
            HttpStatus.UNAUTHORIZED,
            "엑세스 권한이 없습니다.",
            "000102"
    ),
    FORBIDDEN(
            HttpStatus.FORBIDDEN,
            "해당 요청으로 접근할 수 없습니다.",
            "000103"
    ),
    NOT_FOUND(
            HttpStatus.NOT_FOUND,
            "해당 응답을 찾지 못했습니다.",
            "000104"
    ),
    METHOD_NOT_ALLOWED(
            HttpStatus.METHOD_NOT_ALLOWED,
            "해당 요청 메소드는 지원하지 않습니다.",
            "000105"
    ),
    TOO_MANY_REQUESTS(
            HttpStatus.TOO_MANY_REQUESTS,
            "너무 많은 요청을 보냈습니다.",
            "000106"
    ),
    NOT_ACCEPTABLE(
            HttpStatus.NOT_ACCEPTABLE,
            "해당 헤더는 수행할 수 없습니다.",
            "000107"
    ),
    REQUEST_TIMEOUT(
            HttpStatus.REQUEST_TIMEOUT,
            "요청시간이 초과되었습니다.",
            "000108"
    ),
    CONFLICT(
            HttpStatus.CONFLICT,
            "서버에 해당 리소스 상태가 이미 존재합니다.",
            "000109"
    ),
    URI_TOO_LONG(
            HttpStatus.URI_TOO_LONG,
            "요청 주소의 허용길이가 초과되었습니다.",
            "000110"
    ),
    UNSUPPORTED_MEDIA_TYPE(
            HttpStatus.UNSUPPORTED_MEDIA_TYPE,
            "지원하지 않는 미디어타입 입니다.",
            "000111"
    ),
    GONE(
            HttpStatus.GONE,
            "해당 요청은 더 이상 사용할 수 없습니다.",
            "000112"
    ),
    LENGTH_REQUIRED(
            HttpStatus.LENGTH_REQUIRED,
            "요청 헤더에 Content-Length를 지정해야 합니다.",
            "000113"
    ),
    PAYLOAD_TOO_LARGE(
            HttpStatus.PAYLOAD_TOO_LARGE,
            "요청 메시지가 너무 큽니다.",
            "000114"
    ),
    PRECONDITION_FAILED(
            HttpStatus.PRECONDITION_FAILED,
            "해당 요청의 사전 조건이 맞지 않습니다.",
            "000115"
    ),
    PROXY_AUTHENTICATION_REQUIRED(
            HttpStatus.PROXY_AUTHENTICATION_REQUIRED,
            "프록시 서버 인증이 필요합니다.",
            "000116"
    ),
    REQUESTED_RANGE_NOT_SATISFIABLE(
            HttpStatus.REQUESTED_RANGE_NOT_SATISFIABLE,
            "서버가 요청한 범위를 제공할 수 없습니다.",
            "000117"
    ),
    EXPECTATION_FAILED(
            HttpStatus.EXPECTATION_FAILED,
            "Expect 헤더를 처리하지 못했습니다.",
            "000118"
    ),
    UNPROCESSABLE_ENTITY(
            HttpStatus.UNPROCESSABLE_ENTITY,
            "요청 인코딩을 확인해주세요, 처리할 수 없는 형태 입니다.",
            "000119"
    ),
    LOCKED(
            HttpStatus.LOCKED,
            "해당 요청 메소드는 현재 잠겨있습니다.",
            "000120"
    ),
    UPGRADE_REQUIRED(
            HttpStatus.UPGRADE_REQUIRED,
            "다른 프로토콜을 이용해주세요",
            "000121"
    ),
    PRECONDITION_REQUIRED(
            HttpStatus.PRECONDITION_REQUIRED,
            "헤더 조건과 일치 하지 않는 요청입니다.",
            "000122"
    ),

    REQUEST_HEADER_FIELDS_TOO_LARGE(
            HttpStatus.REQUEST_HEADER_FIELDS_TOO_LARGE,
            "요청 헤드 필드가 너무 큽니다.",
            "000123"
    );

    private final HttpStatus httpStatus;
    private final String message;
    private final String code;

    @Override
    public ErrorResponse getError() {
        return ErrorResponse.builder()
                .status(httpStatus.value())
                .code(code)
                .message(message)
                .build();
    }
}
