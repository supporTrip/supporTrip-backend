package com.supportrip.core.exchange.exception;

import com.supportrip.core.error.ErrorInfo;
import com.supportrip.core.error.exception.BusinessException;
import org.springframework.boot.logging.LogLevel;

public class AlreadyCompletedTradingException extends BusinessException {
    public AlreadyCompletedTradingException() {
        super(ErrorInfo.ALREADY_COMPLETED_TRADING, LogLevel.ERROR);
    }
}
