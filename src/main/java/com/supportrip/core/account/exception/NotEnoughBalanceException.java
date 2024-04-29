package com.supportrip.core.account.exception;

import com.supportrip.core.error.ErrorInfo;
import com.supportrip.core.error.exception.BusinessException;
import org.springframework.boot.logging.LogLevel;

public class NotEnoughBalanceException extends BusinessException {
    public NotEnoughBalanceException() {
        super(ErrorInfo.NOT_ENOUGH_BALANCE, LogLevel.ERROR);
    }
}
