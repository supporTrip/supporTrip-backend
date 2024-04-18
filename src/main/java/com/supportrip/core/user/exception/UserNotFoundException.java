package com.supportrip.core.user.exception;

import com.supportrip.core.error.ErrorInfo;
import com.supportrip.core.error.exception.BusinessException;
import org.springframework.boot.logging.LogLevel;

public class UserNotFoundException extends BusinessException {
    public UserNotFoundException() {
        this(LogLevel.WARN);
    }

    public UserNotFoundException(LogLevel logLevel) {
        super(ErrorInfo.USER_NOT_FOUND, logLevel);
    }
}
