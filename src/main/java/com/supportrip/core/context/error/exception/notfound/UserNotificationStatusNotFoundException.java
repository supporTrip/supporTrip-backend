package com.supportrip.core.context.error.exception.notfound;

import com.supportrip.core.context.error.ErrorInfo;
import com.supportrip.core.context.error.exception.BusinessException;
import org.springframework.boot.logging.LogLevel;

public class UserNotificationStatusNotFoundException extends BusinessException {

    public UserNotificationStatusNotFoundException() {
        super(ErrorInfo.USER_NOTIFICATION_STATUS_NOT_FOUND, LogLevel.ERROR);
    }
}
