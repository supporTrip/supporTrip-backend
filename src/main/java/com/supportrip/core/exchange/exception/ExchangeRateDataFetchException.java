package com.supportrip.core.exchange.exception;

import com.supportrip.core.error.ErrorInfo;
import com.supportrip.core.error.exception.BusinessException;
import org.springframework.boot.logging.LogLevel;

public class ExchangeRateDataFetchException extends BusinessException {
    public ExchangeRateDataFetchException() {
        super(ErrorInfo.EXCHANGE_RATE_DATA_FETCH, LogLevel.ERROR);
    }
}
