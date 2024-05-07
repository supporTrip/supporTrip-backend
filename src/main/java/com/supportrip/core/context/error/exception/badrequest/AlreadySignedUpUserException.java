package com.supportrip.core.context.error.exception.badrequest;

import com.supportrip.core.context.error.ErrorInfo;
import com.supportrip.core.context.error.exception.BusinessException;
import org.springframework.boot.logging.LogLevel;

public class AlreadySignedUpUserException extends BusinessException {
    public AlreadySignedUpUserException() {
        super(ErrorInfo.ALREADY_SIGNED_UP_USER, LogLevel.WARN);
    }
}
