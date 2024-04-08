package com.supportrip.core.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
public enum ErrorInfo {
    // 400
    INVALID_INPUT(BAD_REQUEST, "400-01", "잘못된 입력입니다."),

    // 401
    UNAUTHORIZED_REQUEST(UNAUTHORIZED, "401-01", "로그인이 필요한 요청입니다."),
    EXPIRED_TOKEN(UNAUTHORIZED, "401-02", "만료된 토큰입니다."),
    INVALID_TOKEN_TYPE(UNAUTHORIZED, "401-03", "식별되지 않는 토큰입니다."),


    // 500
    UNHANDLED_ERROR(INTERNAL_SERVER_ERROR, "500-01", "알 수 없는 오류가 발생했습니다. 관리자에게 연락해주세요.");

    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String message;

    ErrorInfo(HttpStatus httpStatus, String errorCode, String message) {
        this.httpStatus = httpStatus;
        this.errorCode = errorCode;
        this.message = message;
    }
}
