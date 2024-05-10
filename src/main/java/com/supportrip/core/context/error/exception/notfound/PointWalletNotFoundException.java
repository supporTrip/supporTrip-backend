package com.supportrip.core.context.error.exception.notfound;

import com.supportrip.core.context.error.ErrorInfo;
import com.supportrip.core.context.error.exception.BusinessException;
import org.springframework.boot.logging.LogLevel;

public class PointWalletNotFoundException extends BusinessException {
    public PointWalletNotFoundException() {
        super(ErrorInfo.POINT_WALLET_NOT_FOUND, LogLevel.ERROR);
    }
}
