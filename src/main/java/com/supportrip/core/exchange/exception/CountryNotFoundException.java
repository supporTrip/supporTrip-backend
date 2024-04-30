package com.supportrip.core.exchange.exception;

import com.supportrip.core.error.ErrorInfo;
import com.supportrip.core.error.exception.BusinessException;
import org.springframework.boot.logging.LogLevel;

public class CountryNotFoundException extends BusinessException {
    public CountryNotFoundException() {
        super(ErrorInfo.COUNTRY_NOT_FOUND, LogLevel.ERROR);
    }
}
