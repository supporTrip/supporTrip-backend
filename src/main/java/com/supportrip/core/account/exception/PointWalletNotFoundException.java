package com.supportrip.core.account.exception;

import com.supportrip.core.error.ErrorInfo;
import com.supportrip.core.error.exception.BusinessException;
import org.springframework.boot.logging.LogLevel;

public class PointWalletNotFoundException extends BusinessException {
    public PointWalletNotFoundException() {
        super(ErrorInfo.POINT_WALLET_NOT_FOUND, LogLevel.ERROR);
    }
}
