package com.supportrip.core.exchange.exception;

import com.supportrip.core.error.ErrorInfo;
import com.supportrip.core.error.exception.BusinessException;
import org.springframework.boot.logging.LogLevel;

public class NotEnoughPointException extends BusinessException {
    public NotEnoughPointException() {
        super(ErrorInfo.NOT_ENOUGH_POINT, LogLevel.WARN);
    }
}
