package com.supportrip.core.context.error.exception.badrequest;

import com.supportrip.core.context.error.ErrorInfo;
import com.supportrip.core.context.error.exception.BusinessException;
import org.springframework.boot.logging.LogLevel;

public class AlreadyCompletedTradingException extends BusinessException {
    public AlreadyCompletedTradingException() {
        super(ErrorInfo.ALREADY_COMPLETED_TRADING, LogLevel.ERROR);
    }
}
