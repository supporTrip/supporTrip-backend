package com.supportrip.core.user.dto;

import com.supportrip.core.user.domain.Gender;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class SignUpRequest {
    @NotBlank(message = "이름을 입력해주세요.")
    private String name;

    @NotBlank(message = "이메일을 입력해주세요.")
    private String email;

    @NotBlank(message = "휴대폰 번호를 입력해주세요.")
    private String phoneNumber;

    @NotNull(message = "생년월일을 입력해주세요.")
    private LocalDate birthDay;

    @NotNull(message = "성별을 선택해주세요.")
    private Gender gender;

    @NotNull(message = "핀번호를 입력해주세요.")
    private String pinNumber;

    @NotNull(message = "만 14세 이상 동의에 체크해주세요.")
    private Boolean consentAbove14;

    @NotNull(message = "서포트립 이용약관 동의에 체크해주세요.")
    private Boolean serviceTermsConsent;

    @NotNull(message = "개인 정보 이용 동의에 체크해주세요.")
    private Boolean consentPersonalInfo;

    private Boolean adInfoConsent;

    private Boolean myDataConsentPersonalInfo;

    @Builder(access = AccessLevel.PRIVATE)
    public SignUpRequest(String name, String email, String phoneNumber, LocalDate birthDay, Gender gender, String pinNumber, Boolean consentAbove14, Boolean serviceTermsConsent, Boolean consentPersonalInfo, Boolean adInfoConsent, Boolean myDataConsentPersonalInfo) {
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.birthDay = birthDay;
        this.gender = gender;
        this.pinNumber = pinNumber;
        this.consentAbove14 = consentAbove14;
        this.serviceTermsConsent = serviceTermsConsent;
        this.consentPersonalInfo = consentPersonalInfo;
        this.adInfoConsent = adInfoConsent;
        this.myDataConsentPersonalInfo = myDataConsentPersonalInfo;
    }

    public static SignUpRequest of(String name, String email, String phoneNumber, LocalDate birthDay, Gender gender, String pinNumber, Boolean consentAbove14, Boolean serviceTermsConsent, Boolean consentPersonalInfo, Boolean adInfoConsent, Boolean myDataConsentPersonalInfo) {
        return SignUpRequest.builder()
                .name(name)
                .email(email)
                .phoneNumber(phoneNumber)
                .birthDay(birthDay)
                .gender(gender)
                .pinNumber(pinNumber)
                .consentAbove14(consentAbove14)
                .serviceTermsConsent(serviceTermsConsent)
                .consentPersonalInfo(consentPersonalInfo)
                .adInfoConsent(resolveSelectableConsent(adInfoConsent))
                .myDataConsentPersonalInfo(resolveSelectableConsent(adInfoConsent))
                .build();
    }

    private static Boolean resolveSelectableConsent(Boolean consent) {
        return consent == null ? Boolean.FALSE : consent;
    }
}