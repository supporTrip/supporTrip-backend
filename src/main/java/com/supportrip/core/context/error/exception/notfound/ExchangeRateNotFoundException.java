package com.supportrip.core.context.error.exception.notfound;

import com.supportrip.core.context.error.ErrorInfo;
import com.supportrip.core.context.error.exception.BusinessException;
import org.springframework.boot.logging.LogLevel;

public class ExchangeRateNotFoundException extends BusinessException {
    public ExchangeRateNotFoundException() {
        super(ErrorInfo.EXCHANGE_RATE_NOT_FOUND, LogLevel.ERROR);
    }
}
