package com.supportrip.core.insurance.exception;

import com.supportrip.core.error.ErrorInfo;
import com.supportrip.core.error.exception.BusinessException;
import org.springframework.boot.logging.LogLevel;

public class NotFoundFlightInsuranceException extends BusinessException {

    public NotFoundFlightInsuranceException() {
        this(LogLevel.ERROR);
    }

    public NotFoundFlightInsuranceException(LogLevel logLevel) {
        super(ErrorInfo.FLIGHT_INSURANCE_NOT_FOUND, logLevel);
    }
}
