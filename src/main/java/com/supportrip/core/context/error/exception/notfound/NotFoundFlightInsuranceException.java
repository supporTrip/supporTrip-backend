package com.supportrip.core.context.error.exception.notfound;

import com.supportrip.core.context.error.ErrorInfo;
import com.supportrip.core.context.error.exception.BusinessException;
import org.springframework.boot.logging.LogLevel;

public class NotFoundFlightInsuranceException extends BusinessException {

    public NotFoundFlightInsuranceException() {
        this(LogLevel.ERROR);
    }

    public NotFoundFlightInsuranceException(LogLevel logLevel) {
        super(ErrorInfo.FLIGHT_INSURANCE_NOT_FOUND, logLevel);
    }
}
