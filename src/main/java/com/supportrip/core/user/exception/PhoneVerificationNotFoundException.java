package com.supportrip.core.user.exception;

import com.supportrip.core.error.ErrorInfo;
import com.supportrip.core.error.exception.BusinessException;
import org.springframework.boot.logging.LogLevel;

public class PhoneVerificationNotFoundException extends BusinessException {
    public PhoneVerificationNotFoundException() {
        super(ErrorInfo.PHONE_VERIFICATION_NOT_FOUND, LogLevel.WARN);
    }
}
