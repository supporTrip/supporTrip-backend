package com.supportrip.core.account.exception;

import com.supportrip.core.error.ErrorInfo;
import com.supportrip.core.error.exception.BusinessException;
import org.springframework.boot.logging.LogLevel;

public class ForeignAccountNotFoundException extends BusinessException {
    public ForeignAccountNotFoundException() {
        super(ErrorInfo.FOREIGN_ACCOUNT_NOT_FOUND, LogLevel.ERROR);
    }
}
