package com.supportrip.core.context.error.exception.notfound;

import com.supportrip.core.context.error.ErrorInfo;
import com.supportrip.core.context.error.exception.BusinessException;
import org.springframework.boot.logging.LogLevel;

public class ForeignAccountNotFoundException extends BusinessException {
    public ForeignAccountNotFoundException() {
        super(ErrorInfo.FOREIGN_ACCOUNT_NOT_FOUND, LogLevel.ERROR);
    }
}
