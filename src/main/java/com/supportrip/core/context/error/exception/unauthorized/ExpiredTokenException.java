package com.supportrip.core.context.error.exception.unauthorized;

import com.supportrip.core.context.error.ErrorInfo;
import com.supportrip.core.context.error.exception.BusinessException;
import org.springframework.boot.logging.LogLevel;

public class ExpiredTokenException extends BusinessException {
    public ExpiredTokenException() {
        super(ErrorInfo.EXPIRED_TOKEN, LogLevel.INFO);
    }
}
