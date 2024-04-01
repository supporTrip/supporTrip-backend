package com.supportrip.core.error;

import com.supportrip.core.error.exception.BusinessException;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ErrorResponse {
    private final LocalDateTime serverTime;
    private final String message;
    private final String errorCode;

    public ErrorResponse(String message, String errorCode) {
        this.serverTime = LocalDateTime.now();
        this.message = message;
        this.errorCode = errorCode;
    }

    public static ErrorResponse from(ErrorInfo errorInfo) {
        return new ErrorResponse(errorInfo.getMessage(), errorInfo.getErrorCode());
    }

    public static ErrorResponse from(BusinessException exception) {
        return new ErrorResponse(exception.getMessage(), exception.getErrorInfo().getErrorCode());
    }
}
