package com.supportrip.core.insurance.exception;

import com.supportrip.core.error.ErrorInfo;
import com.supportrip.core.error.exception.BusinessException;
import org.springframework.boot.logging.LogLevel;

public class NotFoundInsuranceCompanyException extends BusinessException {

    public NotFoundInsuranceCompanyException(){
        this(LogLevel.ERROR);
    }

    public NotFoundInsuranceCompanyException(LogLevel logLevel) {
        super(ErrorInfo.INSURANCE_COMPANY_NOT_FOUND, logLevel);
    }
}
