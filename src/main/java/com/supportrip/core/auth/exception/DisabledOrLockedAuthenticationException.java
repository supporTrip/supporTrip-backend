package com.supportrip.core.auth.exception;

import com.supportrip.core.error.ErrorInfo;
import com.supportrip.core.error.exception.BusinessException;
import org.springframework.boot.logging.LogLevel;

public class DisabledOrLockedAuthenticationException extends BusinessException {
    public DisabledOrLockedAuthenticationException() {
        super(ErrorInfo.DISABLED_OR_LOCKED_AUTHENTICATION, LogLevel.WARN);
    }
}
