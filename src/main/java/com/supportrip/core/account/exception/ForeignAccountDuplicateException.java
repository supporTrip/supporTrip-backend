package com.supportrip.core.account.exception;

import com.supportrip.core.error.ErrorInfo;
import com.supportrip.core.error.exception.BusinessException;
import org.springframework.boot.logging.LogLevel;

public class ForeignAccountDuplicateException extends BusinessException {
    public ForeignAccountDuplicateException() {
        this(LogLevel.WARN);
    }

    public ForeignAccountDuplicateException(LogLevel logLevel){super(ErrorInfo.DUPLICATE_FOREIGN_ACCOUNT, logLevel);}
}
