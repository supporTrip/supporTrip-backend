package com.supportrip.core.context.error.exception.badrequest;

import com.supportrip.core.context.error.ErrorInfo;
import com.supportrip.core.context.error.exception.BusinessException;
import org.springframework.boot.logging.LogLevel;

public class NotEnoughPointException extends BusinessException {
    public NotEnoughPointException() {
        super(ErrorInfo.NOT_ENOUGH_POINT, LogLevel.WARN);
    }
}
