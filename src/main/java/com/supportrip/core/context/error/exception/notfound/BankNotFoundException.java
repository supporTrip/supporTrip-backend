package com.supportrip.core.context.error.exception.notfound;

import com.supportrip.core.context.error.ErrorInfo;
import com.supportrip.core.context.error.exception.BusinessException;
import org.springframework.boot.logging.LogLevel;

public class BankNotFoundException extends BusinessException {
    public BankNotFoundException() {
        super(ErrorInfo.BANK_NOT_FOUND, LogLevel.ERROR);
    }
}
