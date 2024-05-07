package com.supportrip.core.context.error.exception.notfound;

import com.supportrip.core.context.error.ErrorInfo;
import com.supportrip.core.context.error.exception.BusinessException;
import org.springframework.boot.logging.LogLevel;

public class PhoneVerificationNotFoundException extends BusinessException {
    public PhoneVerificationNotFoundException() {
        super(ErrorInfo.PHONE_VERIFICATION_NOT_FOUND, LogLevel.WARN);
    }
}
