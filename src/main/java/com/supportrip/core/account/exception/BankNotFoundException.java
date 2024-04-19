package com.supportrip.core.account.exception;

import com.supportrip.core.error.ErrorInfo;
import com.supportrip.core.error.exception.BusinessException;
import org.springframework.boot.logging.LogLevel;

public class BankNotFoundException extends BusinessException {
    public BankNotFoundException() {
        super(ErrorInfo.BANK_NOT_FOUND, LogLevel.ERROR);
    }
}
