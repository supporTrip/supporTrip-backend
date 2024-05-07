package com.supportrip.core.context.error.exception.internalservererror;

import com.supportrip.core.context.error.ErrorInfo;
import com.supportrip.core.context.error.exception.BusinessException;
import org.springframework.boot.logging.LogLevel;

public class OutdatedExchangeRateException extends BusinessException {
    public OutdatedExchangeRateException() {
        super(ErrorInfo.OUTDATED_EXCHANGE_RATE, LogLevel.ERROR);
    }
}
