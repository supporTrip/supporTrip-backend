package com.supportrip.core.context.error.exception.unauthorized;

import com.supportrip.core.context.error.ErrorInfo;
import com.supportrip.core.context.error.exception.BusinessException;
import org.springframework.boot.logging.LogLevel;

public class InvalidTokenTypeException extends BusinessException {
    public InvalidTokenTypeException() {
        super(ErrorInfo.INVALID_TOKEN_TYPE, LogLevel.INFO);
    }
}
