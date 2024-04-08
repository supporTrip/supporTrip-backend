package com.supportrip.core.auth.jwt.exception;

import com.supportrip.core.error.ErrorInfo;
import com.supportrip.core.error.exception.BusinessException;
import org.springframework.boot.logging.LogLevel;

public class InvalidTokenTypeException extends BusinessException {
    public InvalidTokenTypeException() {
        super(ErrorInfo.INVALID_TOKEN_TYPE, LogLevel.INFO);
    }
}
