package com.supportrip.core.system.core.user.internal.presentation.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PinNumberVerificationRequest {
    @NotEmpty(message = "PIN 번호를 입력해주세요.")
    private String pinNumber;

    @Builder(access = AccessLevel.PRIVATE)
    private PinNumberVerificationRequest(String pinNumber) {
        this.pinNumber = pinNumber;
    }

    public static PinNumberVerificationRequest from(String pinNumber) {
        return PinNumberVerificationRequest.builder()
                .pinNumber(pinNumber)
                .build();
    }
}
