package com.supportrip.core.user.exception;

import com.supportrip.core.error.ErrorInfo;
import com.supportrip.core.error.exception.BusinessException;
import org.springframework.boot.logging.LogLevel;

public class UserSocialsNotFoundException extends BusinessException {
    public UserSocialsNotFoundException() {
        super(ErrorInfo.USER_SOCIALS_NOT_FOUND, LogLevel.ERROR);
    }
}
