package com.supportrip.core.exchange.exception;

import com.supportrip.core.error.ErrorInfo;
import com.supportrip.core.error.exception.BusinessException;
import org.springframework.boot.logging.LogLevel;

public class CountryNotFoundException extends BusinessException {
    public CountryNotFoundException() {
        this(LogLevel.WARN);
    }

    public CountryNotFoundException(LogLevel logLevel) {
        super(ErrorInfo.COUNTRY_NOT_FOUND, logLevel);
    }
}
