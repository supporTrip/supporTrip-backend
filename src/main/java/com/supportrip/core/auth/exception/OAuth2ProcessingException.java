package com.supportrip.core.auth.exception;

import com.supportrip.core.error.ErrorInfo;
import com.supportrip.core.error.exception.BusinessException;
import org.springframework.boot.logging.LogLevel;

public class OAuth2ProcessingException extends BusinessException {
    public OAuth2ProcessingException() {
        super(ErrorInfo.OAUTH2_PROCSSING_ERROR, LogLevel.WARN);
    }
}
