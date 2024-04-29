package com.supportrip.core.user.exception;

import com.supportrip.core.error.ErrorInfo;
import com.supportrip.core.error.exception.BusinessException;
import org.springframework.boot.logging.LogLevel;

public class AlreadyVerifiedException extends BusinessException {
    public AlreadyVerifiedException() {
        super(ErrorInfo.ALREADY_VERIFIED, LogLevel.WARN);
    }
}
