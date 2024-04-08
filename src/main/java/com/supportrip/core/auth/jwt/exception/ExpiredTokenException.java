package com.supportrip.core.auth.jwt.exception;

import com.supportrip.core.error.ErrorInfo;
import com.supportrip.core.error.exception.BusinessException;
import org.springframework.boot.logging.LogLevel;

public class ExpiredTokenException extends BusinessException {
    public ExpiredTokenException() {
        super(ErrorInfo.EXPIRED_TOKEN, LogLevel.INFO);
    }
}
