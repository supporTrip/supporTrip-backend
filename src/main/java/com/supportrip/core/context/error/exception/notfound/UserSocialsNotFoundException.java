package com.supportrip.core.context.error.exception.notfound;

import com.supportrip.core.context.error.ErrorInfo;
import com.supportrip.core.context.error.exception.BusinessException;
import org.springframework.boot.logging.LogLevel;

public class UserSocialsNotFoundException extends BusinessException {
    public UserSocialsNotFoundException() {
        super(ErrorInfo.USER_SOCIALS_NOT_FOUND, LogLevel.ERROR);
    }
}
