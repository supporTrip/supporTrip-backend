package com.supportrip.core.context.error.exception;

import com.supportrip.core.context.error.ErrorInfo;
import lombok.Getter;
import org.springframework.boot.logging.LogLevel;

@Getter
public class BusinessException extends RuntimeException {
    protected final ErrorInfo errorInfo;
    protected final LogLevel logLevel;

    public BusinessException(String message, ErrorInfo errorInfo, LogLevel logLevel) {
        super(message);
        this.errorInfo = errorInfo;
        this.logLevel = logLevel;
    }

    public BusinessException(ErrorInfo errorInfo, LogLevel logLevel) {
        this(errorInfo.getMessage(), errorInfo, logLevel);
    }

    public BusinessException(String message, ErrorInfo errorInfo) {
        this(message, errorInfo, LogLevel.ERROR);
    }

    public BusinessException(ErrorInfo errorInfo) {
        this(errorInfo.getMessage(), errorInfo, LogLevel.ERROR);
    }
}
