package com.supportrip.core.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LoginRequest {
    @NotBlank(message = "code를 입력해주세요.")
    private String code;

    @Builder(access = AccessLevel.PRIVATE)
    private LoginRequest(String code) {
        this.code = code;
    }

    public static LoginRequest from(String code) {
        return LoginRequest.builder()
                .code(code)
                .build();
    }
}
