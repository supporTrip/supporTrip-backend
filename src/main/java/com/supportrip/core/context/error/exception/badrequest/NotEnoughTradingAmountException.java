package com.supportrip.core.context.error.exception.badrequest;

import com.supportrip.core.context.error.ErrorInfo;
import com.supportrip.core.context.error.exception.BusinessException;
import org.springframework.boot.logging.LogLevel;

public class NotEnoughTradingAmountException extends BusinessException {
    public NotEnoughTradingAmountException() {
        super(ErrorInfo.NOT_ENOUGH_TRADING_AMOUNT, LogLevel.ERROR);
    }
}
