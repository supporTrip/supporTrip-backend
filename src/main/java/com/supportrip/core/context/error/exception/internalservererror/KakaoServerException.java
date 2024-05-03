package com.supportrip.core.context.error.exception.internalservererror;

import com.supportrip.core.context.error.ErrorInfo;
import com.supportrip.core.context.error.exception.BusinessException;
import org.springframework.boot.logging.LogLevel;

public class KakaoServerException extends BusinessException {
    public KakaoServerException() {
        super(ErrorInfo.KAKAO_SERVER_ERROR, LogLevel.ERROR);
    }
}
