package com.supportrip.core.system.core.user.internal.presentation.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class VerifyPhoneVerificationCodeRequest {
    private String code;

    public VerifyPhoneVerificationCodeRequest(String code) {
        this.code = code;
    }
}
