package com.supportrip.core.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
public enum ErrorInfo {
    // 400
    INVALID_INPUT(BAD_REQUEST, "400-01", "잘못된 입력입니다."),
    ALREADY_SIGNED_UP_USER(BAD_REQUEST, "400-02", "이미 회원 가입이 완료된 계정입니다."),
    DUPLICATE_FOREIGN_ACCOUNT(BAD_REQUEST, "400-03", "중복된 계좌 생성 요청입니다."),

    // 401
    UNAUTHORIZED_REQUEST(UNAUTHORIZED, "401-01", "로그인이 필요한 요청입니다."),
    OAUTH2_PROCSSING_ERROR(UNAUTHORIZED, "401-02", "소셜 로그인을 진행하던 중 에러가 발생했습니다."),
    EXPIRED_TOKEN(UNAUTHORIZED, "401-03", "만료된 토큰입니다."),
    INVALID_TOKEN_TYPE(UNAUTHORIZED, "401-04", "식별되지 않는 토큰입니다."),

    // 403
    ACCESS_DENIED(FORBIDDEN, "403-01", "권한이 없어 해당 요청을 처리할 수 없습니다."),

    // 404
    USER_NOT_FOUND(NOT_FOUND, "404-01", "해당 유저 정보를 찾을 수 없습니다."),
    USER_SOCIALS_NOT_FOUND(NOT_FOUND, "404-02", "해당 유저의 소셜 로그인 정보를 찾을 수 없습니다."),
    BANK_NOT_FOUND(NOT_FOUND, "404-03", "해당 은행 정보를 찾을 수 없습니다."),
    FLIGHT_INSURANCE_NOT_FOUND(NOT_FOUND, "404-04", "해당 보험 상품를 찾을 수 없습니다."),
    CURRENCY_NOT_FOUND(NOT_FOUND, "404-05", "해당 통화 정보를 찾을 수 없습니다."),
    LINKED_ACCOUNT_NOT_FOUND(NOT_FOUND, "404-06", "해당 연결계좌 정보를 찾을 수 없습니다."),

    // 500
    UNHANDLED_ERROR(INTERNAL_SERVER_ERROR, "500-01", "알 수 없는 오류가 발생했습니다. 관리자에게 연락해주세요."),
    KAKAO_SERVER_ERROR(INTERNAL_SERVER_ERROR, "500-02", "카카오 서버로부터 인증 정보를 가져오지 못했습니다.");

    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String message;

    ErrorInfo(HttpStatus httpStatus, String errorCode, String message) {
        this.httpStatus = httpStatus;
        this.errorCode = errorCode;
        this.message = message;
    }
}