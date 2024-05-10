package com.supportrip.core.context.error.exception.internalservererror;

import com.supportrip.core.context.error.ErrorInfo;
import com.supportrip.core.context.error.exception.BusinessException;
import org.springframework.boot.logging.LogLevel;

public class ExchangeRateDataFetchException extends BusinessException {
    public ExchangeRateDataFetchException() {
        super(ErrorInfo.EXCHANGE_RATE_DATA_FETCH, LogLevel.ERROR);
    }
}
