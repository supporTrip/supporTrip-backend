package com.supportrip.core.exchange.exception;

import com.supportrip.core.error.ErrorInfo;
import com.supportrip.core.error.exception.BusinessException;
import org.springframework.boot.logging.LogLevel;

public class ExchangeAccessDeniedException extends BusinessException {
    public ExchangeAccessDeniedException() {
        this(LogLevel.WARN);
    }

    public ExchangeAccessDeniedException(LogLevel logLevel) {
        super(ErrorInfo.EXCHANGE_ACCESS_DENIED, logLevel);
    }
}
