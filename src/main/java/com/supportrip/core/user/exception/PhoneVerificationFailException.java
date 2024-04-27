package com.supportrip.core.user.exception;

import com.supportrip.core.error.ErrorInfo;
import com.supportrip.core.error.exception.BusinessException;
import org.springframework.boot.logging.LogLevel;

public class PhoneVerificationFailException extends BusinessException {
    public PhoneVerificationFailException() {
        super(ErrorInfo.PHONE_VERIFICATION_FAIL, LogLevel.WARN);
    }
}
