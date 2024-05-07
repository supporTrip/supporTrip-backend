package com.supportrip.core.context.error.exception.notfound;

import com.supportrip.core.context.error.ErrorInfo;
import com.supportrip.core.context.error.exception.BusinessException;
import org.springframework.boot.logging.LogLevel;

public class CurrencyNotFoundException extends BusinessException {
    public CurrencyNotFoundException() {
        super(ErrorInfo.CURRENCY_NOT_FOUND, LogLevel.ERROR);
    }
}
