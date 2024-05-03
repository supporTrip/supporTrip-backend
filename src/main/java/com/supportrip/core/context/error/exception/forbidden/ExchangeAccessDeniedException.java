package com.supportrip.core.context.error.exception.forbidden;

import com.supportrip.core.context.error.ErrorInfo;
import com.supportrip.core.context.error.exception.BusinessException;
import org.springframework.boot.logging.LogLevel;

public class ExchangeAccessDeniedException extends BusinessException {
    public ExchangeAccessDeniedException() {
        this(LogLevel.WARN);
    }

    public ExchangeAccessDeniedException(LogLevel logLevel) {
        super(ErrorInfo.EXCHANGE_ACCESS_DENIED, logLevel);
    }
}
