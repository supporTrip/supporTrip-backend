package com.supportrip.core.context.error.exception.notfound;

import com.supportrip.core.context.error.ErrorInfo;
import com.supportrip.core.context.error.exception.BusinessException;
import org.springframework.boot.logging.LogLevel;

public class NotFoundInsuranceCompanyException extends BusinessException {

    public NotFoundInsuranceCompanyException(){
        this(LogLevel.ERROR);
    }

    public NotFoundInsuranceCompanyException(LogLevel logLevel) {
        super(ErrorInfo.INSURANCE_COMPANY_NOT_FOUND, logLevel);
    }
}
