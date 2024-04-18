package com.supportrip.core.user.exception;

import com.supportrip.core.error.ErrorInfo;
import com.supportrip.core.error.exception.BusinessException;
import org.springframework.boot.logging.LogLevel;

public class AlreadySignedUpUserException extends BusinessException {
    public AlreadySignedUpUserException() {
        super(ErrorInfo.ALREADY_SIGNED_UP_USER, LogLevel.WARN);
    }
}
