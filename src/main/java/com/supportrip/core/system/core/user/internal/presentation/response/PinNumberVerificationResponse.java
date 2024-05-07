package com.supportrip.core.system.core.user.internal.presentation.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
public class PinNumberVerificationResponse {
    private final boolean success;

    @Builder(access = AccessLevel.PRIVATE)
    private PinNumberVerificationResponse(boolean success) {
        this.success = success;
    }

    public static PinNumberVerificationResponse from(boolean success) {
        return PinNumberVerificationResponse.builder()
                .success(success)
                .build();
    }
}
