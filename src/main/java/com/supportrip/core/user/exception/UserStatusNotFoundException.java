package com.supportrip.core.user.exception;

import com.supportrip.core.error.ErrorInfo;
import com.supportrip.core.error.exception.BusinessException;
import org.springframework.boot.logging.LogLevel;

public class UserStatusNotFoundException extends BusinessException {

    public UserStatusNotFoundException() {
        super(ErrorInfo.USER_NOTIFICATION_STATUS_NOT_FOUND, LogLevel.ERROR);
    }
}
