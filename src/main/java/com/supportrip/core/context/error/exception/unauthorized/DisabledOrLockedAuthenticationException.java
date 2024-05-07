package com.supportrip.core.context.error.exception.unauthorized;

import com.supportrip.core.context.error.ErrorInfo;
import com.supportrip.core.context.error.exception.BusinessException;
import org.springframework.boot.logging.LogLevel;

public class DisabledOrLockedAuthenticationException extends BusinessException {
    public DisabledOrLockedAuthenticationException() {
        super(ErrorInfo.DISABLED_OR_LOCKED_AUTHENTICATION, LogLevel.WARN);
    }
}
