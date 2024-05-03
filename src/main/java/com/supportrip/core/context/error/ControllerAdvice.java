package com.supportrip.core.context.error;

import com.supportrip.core.context.error.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.logging.LogLevel;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.supportrip.core.context.error.ErrorInfo.INVALID_INPUT;
import static com.supportrip.core.context.error.ErrorInfo.UNHANDLED_ERROR;

@Slf4j
@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentException(MethodArgumentNotValidException exception,
                                                                       BindingResult bindingResult) {
        String errorMessage = bindingResult.getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .toList()
                .get(0);

        logException("handleMethodArgumentException", exception, LogLevel.WARN);
        ErrorResponse responseBody = new ErrorResponse(errorMessage, INVALID_INPUT.getErrorCode());
        return ResponseEntity.status(INVALID_INPUT.getHttpStatus()).body(responseBody);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException exception) {
        logException("handleBusinessException", exception, exception.getLogLevel());
        ErrorInfo errorInfo = exception.getErrorInfo();
        ErrorResponse responseBody = ErrorResponse.from(errorInfo);
        return ResponseEntity.status(errorInfo.getHttpStatus()).body(responseBody);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnhandledException(Exception exception) {
        logException("handleUnhandledException", exception, LogLevel.ERROR);
        ErrorResponse responseBody = ErrorResponse.from(UNHANDLED_ERROR);
        return ResponseEntity.status(UNHANDLED_ERROR.getHttpStatus()).body(responseBody);
    }

    private void logException(String message, Exception exception, LogLevel logLevel) {
        switch (logLevel) {
            case TRACE -> log.trace(message, exception);
            case DEBUG -> log.debug(message, exception);
            case INFO -> log.info(message, exception);
            case WARN -> log.warn(message, exception);
            default -> log.error(message, exception);
        }
    }
}
