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
    NOT_ENOUGH_POINT(BAD_REQUEST, "400-04", "현재 보유한 포인트가 충분하지 않습니다."),
    NOT_ENOUGH_TRADING_AMOUNT(BAD_REQUEST, "400-05", "현재 남은 금액보다 더 많은 금액을 환전할 수 없습니다."),
    ALREADY_COMPLETED_TRADING(BAD_REQUEST, "400-06", "이미 완료된 환전 거래입니다."),
    PHONE_VERIFICATION_FAIL(BAD_REQUEST, "400-07", "인증 기간이 지났거나, 올바르지 않은 인증 코드입니다."),
    ALREADY_VERIFIED(BAD_REQUEST, "400-08", "이미 휴대폰 인증이 완료되었습니다."),
    NOT_ENOUGH_BALANCE(BAD_REQUEST, "400-09", "연결 계좌에 잔액이 부족합니다."),

    // 401
    UNAUTHORIZED_REQUEST(UNAUTHORIZED, "401-01", "로그인이 필요한 요청입니다."),
    OAUTH2_PROCSSING_ERROR(UNAUTHORIZED, "401-02", "소셜 로그인을 진행하던 중 에러가 발생했습니다."),
    EXPIRED_TOKEN(UNAUTHORIZED, "401-03", "만료된 토큰입니다."),
    INVALID_TOKEN_TYPE(UNAUTHORIZED, "401-04", "식별되지 않는 토큰입니다."),
    DISABLED_OR_LOCKED_AUTHENTICATION(UNAUTHORIZED, "401-05", "차단되었거나 잠긴 계정입니다. 관리자에게 연락해주세요."),

    // 403
    ACCESS_DENIED(FORBIDDEN, "403-01", "권한이 없어 해당 요청을 처리할 수 없습니다."),
    EXCHANGE_ACCESS_DENIED(FORBIDDEN, "403-02", "외화 계좌가 없어 환전 거래를 할 수 없습니다."),

    // 404
    USER_NOT_FOUND(NOT_FOUND, "404-01", "해당 유저 정보를 찾을 수 없습니다."),
    USER_SOCIALS_NOT_FOUND(NOT_FOUND, "404-02", "해당 유저의 소셜 로그인 정보를 찾을 수 없습니다."),
    BANK_NOT_FOUND(NOT_FOUND, "404-03", "해당 은행 정보를 찾을 수 없습니다."),
    FLIGHT_INSURANCE_NOT_FOUND(NOT_FOUND, "404-04", "해당 보험 상품를 찾을 수 없습니다."),
    CURRENCY_NOT_FOUND(NOT_FOUND, "404-05", "해당 통화 정보를 찾을 수 없습니다."),
    EXCHANGE_RATE_NOT_FOUND(NOT_FOUND, "404-06", "해당 환율 정보를 찾을 수 없습니다."),
    POINT_WALLET_NOT_FOUND(NOT_FOUND, "404-07", "해당 포인트 지갑을 찾을 수 없습니다."),
    FOREIGN_ACCOUNT_NOT_FOUND(NOT_FOUND, "404-08", "해당 외화 계좌를 찾을 수 없습니다."),
    LINKED_ACCOUNT_NOT_FOUND(NOT_FOUND, "404-09", "해당 연결계좌 정보를 찾을 수 없습니다."),
    INSURANCE_COMPANY_NOT_FOUND(NOT_FOUND, "404-10", "해당 보험사를 찾을 수 없습니다."),
    PHONE_VERIFICATION_NOT_FOUND(NOT_FOUND, "404-11", "해당 휴대폰 인증 정보를 찾을 수 없습니다."),
    USER_NOTIFICATION_STATUS_NOT_FOUND(NOT_FOUND, "404-12", "해당 유저의 알람 설정 상태를 찾을 수 없습니다."),
    COUNTRY_NOT_FOUND(NOT_FOUND, "404-13", "해당 국가 정보를 찾을 수 없습니다."),
    FOREIGN_CURRENCY_WALLET_NOT_FOUND(NOT_FOUND, "404-14", "해당 외화 계좌에 대한 외화 지갑을 찾을 수 없습니다."),

    // 500
    UNHANDLED_ERROR(INTERNAL_SERVER_ERROR, "500-01", "알 수 없는 오류가 발생했습니다. 관리자에게 연락해주세요."),
    KAKAO_SERVER_ERROR(INTERNAL_SERVER_ERROR, "500-02", "카카오 서버로부터 인증 정보를 가져오지 못했습니다."),
    OUTDATED_EXCHANGE_RATE(INTERNAL_SERVER_ERROR, "500-03", "이전 환율 정보로 인해 거래를 진행할 수 없습니다."),
    EXCHANGE_RATE_DATA_FETCH(INTERNAL_SERVER_ERROR, "500-04", "오늘은 수출입은행으로부터 환율 정보를 가져올 수 없는 일자입니다. 비영업일이거나 영업 시간 이전일 수 있습니다.");


    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String message;

    ErrorInfo(HttpStatus httpStatus, String errorCode, String message) {
        this.httpStatus = httpStatus;
        this.errorCode = errorCode;
        this.message = message;
    }
}