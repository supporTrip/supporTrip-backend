package com.supportrip.core.auth.jwt.exception;

import com.supportrip.core.error.ErrorInfo;
import com.supportrip.core.error.exception.BusinessException;

public class InvalidTokenTypeException extends BusinessException {
    public InvalidTokenTypeException() {
        super(ErrorInfo.INVALID_TOKEN_TYPE);
    }
}
