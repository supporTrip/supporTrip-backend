package com.supportrip.core.auth.exception;

import com.supportrip.core.error.ErrorInfo;
import com.supportrip.core.error.exception.BusinessException;
import org.springframework.boot.logging.LogLevel;

public class KakaoServerException extends BusinessException {
    public KakaoServerException() {
        super(ErrorInfo.KAKAO_SERVER_ERROR, LogLevel.ERROR);
    }
}
