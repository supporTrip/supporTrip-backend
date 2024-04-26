package com.supportrip.core.account.exception;

import com.supportrip.core.error.ErrorInfo;
import com.supportrip.core.error.exception.BusinessException;
import org.springframework.boot.logging.LogLevel;

public class LinkedAccountNotFoundException extends BusinessException {
    public LinkedAccountNotFoundException() {
        this(LogLevel.WARN);
    }

    public LinkedAccountNotFoundException(LogLevel logLevel){super(ErrorInfo.LINKED_ACCOUNT_NOT_FOUND, logLevel);}
}
