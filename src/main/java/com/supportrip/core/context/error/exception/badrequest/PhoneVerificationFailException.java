package com.supportrip.core.context.error.exception.badrequest;

import com.supportrip.core.context.error.ErrorInfo;
import com.supportrip.core.context.error.exception.BusinessException;
import org.springframework.boot.logging.LogLevel;

public class PhoneVerificationFailException extends BusinessException {
    public PhoneVerificationFailException() {
        super(ErrorInfo.PHONE_VERIFICATION_FAIL, LogLevel.WARN);
    }
}
