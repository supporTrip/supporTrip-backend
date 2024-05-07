package com.supportrip.core.context.error.exception.badrequest;

import com.supportrip.core.context.error.ErrorInfo;
import com.supportrip.core.context.error.exception.BusinessException;
import org.springframework.boot.logging.LogLevel;

public class NotEnoughBalanceException extends BusinessException {
    public NotEnoughBalanceException() {
        super(ErrorInfo.NOT_ENOUGH_BALANCE, LogLevel.ERROR);
    }
}
