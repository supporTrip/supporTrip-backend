package com.supportrip.core.exchange.exception;

import com.supportrip.core.error.ErrorInfo;
import com.supportrip.core.error.exception.BusinessException;
import org.springframework.boot.logging.LogLevel;

public class CurrencyNotFoundException extends BusinessException {
    public CurrencyNotFoundException() {
        super(ErrorInfo.CURRENCY_NOT_FOUND, LogLevel.ERROR);
    }
}
