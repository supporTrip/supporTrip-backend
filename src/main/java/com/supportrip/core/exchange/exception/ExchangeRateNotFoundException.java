package com.supportrip.core.exchange.exception;

import com.supportrip.core.error.ErrorInfo;
import com.supportrip.core.error.exception.BusinessException;
import org.springframework.boot.logging.LogLevel;

public class ExchangeRateNotFoundException extends BusinessException {
    public ExchangeRateNotFoundException() {
        super(ErrorInfo.EXCHANGE_RATE_NOT_FOUND, LogLevel.ERROR);
    }
}
