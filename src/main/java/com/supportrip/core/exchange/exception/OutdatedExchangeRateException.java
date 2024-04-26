package com.supportrip.core.exchange.exception;

import com.supportrip.core.error.ErrorInfo;
import com.supportrip.core.error.exception.BusinessException;
import org.springframework.boot.logging.LogLevel;

public class OutdatedExchangeRateException extends BusinessException {
    public OutdatedExchangeRateException() {
        super(ErrorInfo.OUTDATED_EXCHANGE_RATE, LogLevel.ERROR);
    }
}
