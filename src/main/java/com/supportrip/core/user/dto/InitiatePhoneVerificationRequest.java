package com.supportrip.core.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class InitiatePhoneVerificationRequest {
    @NotBlank(message = "통신사를 입력해주세요.")
    private String telecomCompany;

    @NotBlank(message = "인증 번호를 수신할 전화번호를 입력해주세요.")
    private String phoneNumber;

    @Builder(access = AccessLevel.PRIVATE)
    public InitiatePhoneVerificationRequest(String telecomCompany, String phoneNumber) {
        this.telecomCompany = telecomCompany;
        this.phoneNumber = phoneNumber;
    }

    public String getSmsPhoneNumber() {
        return this.phoneNumber.replace("-", "");
    }
}
