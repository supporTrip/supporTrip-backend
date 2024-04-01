package com.supportrip.core.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Getter
public enum ErrorInfo {
    // 400
    INVALID_INPUT(BAD_REQUEST, "400-01", "잘못된 입력입니다."),

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
