package com.supportrip.core.context.error.exception.badrequest;

import com.supportrip.core.context.error.ErrorInfo;
import com.supportrip.core.context.error.exception.BusinessException;
import org.springframework.boot.logging.LogLevel;

public class AlreadyVerifiedException extends BusinessException {
    public AlreadyVerifiedException() {
        super(ErrorInfo.ALREADY_VERIFIED, LogLevel.WARN);
    }
}
