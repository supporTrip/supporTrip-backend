package com.supportrip.core.exchange.exception;

import com.supportrip.core.error.ErrorInfo;
import com.supportrip.core.error.exception.BusinessException;
import org.springframework.boot.logging.LogLevel;

public class NotEnoughTradingAmountException extends BusinessException {
    public NotEnoughTradingAmountException() {
        super(ErrorInfo.NOT_ENOUGH_TRADING_AMOUNT, LogLevel.ERROR);
    }
}
