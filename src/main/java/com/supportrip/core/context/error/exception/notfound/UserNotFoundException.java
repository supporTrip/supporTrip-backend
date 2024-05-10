package com.supportrip.core.context.error.exception.notfound;

import com.supportrip.core.context.error.ErrorInfo;
import com.supportrip.core.context.error.exception.BusinessException;
import org.springframework.boot.logging.LogLevel;

public class UserNotFoundException extends BusinessException {
    public UserNotFoundException() {
        this(LogLevel.WARN);
    }

    public UserNotFoundException(LogLevel logLevel) {
        super(ErrorInfo.USER_NOT_FOUND, logLevel);
    }
}
