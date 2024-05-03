package com.supportrip.core.context.error.exception.notfound;

import com.supportrip.core.context.error.ErrorInfo;
import com.supportrip.core.context.error.exception.BusinessException;
import org.springframework.boot.logging.LogLevel;

public class LinkedAccountNotFoundException extends BusinessException {
    public LinkedAccountNotFoundException() {
        this(LogLevel.WARN);
    }

    public LinkedAccountNotFoundException(LogLevel logLevel){super(ErrorInfo.LINKED_ACCOUNT_NOT_FOUND, logLevel);}
}
