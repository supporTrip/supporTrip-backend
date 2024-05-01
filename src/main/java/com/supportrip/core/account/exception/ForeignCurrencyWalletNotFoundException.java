package com.supportrip.core.account.exception;

import com.supportrip.core.error.ErrorInfo;
import com.supportrip.core.error.exception.BusinessException;
import org.springframework.boot.logging.LogLevel;

public class ForeignCurrencyWalletNotFoundException extends BusinessException {
    public ForeignCurrencyWalletNotFoundException() {
        super(ErrorInfo.FOREIGN_CURRENCY_WALLET_NOT_FOUND, LogLevel.ERROR);
    }
}
