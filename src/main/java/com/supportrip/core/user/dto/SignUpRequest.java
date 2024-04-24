package com.supportrip.core.user.dto;

import com.supportrip.core.user.domain.Gender;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.Period;

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
    private String birthDay;

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

    @NotBlank(message = "은행을 선택해주세요.")
    private String bank;

    @NotBlank(message = "계좌 번호를 입력해주세요.")
    @Size(min = 10, max = 14, message = "10자에서 14자 사이의 숫자를 입력해주세요.")
    private String bankAccountNumber;

    @NotNull(message = "오픈뱅킹 자동계좌이체 약관 동의에 체크해주세요.")
    private Boolean openBankingAutoTransferConsent;

    @NotNull(message = "오픈뱅킹 금융정보조회 약관 동의에 체크해주세요.")
    private Boolean openBankingFinancialInfoInquiryConsent;

    @NotNull(message = "금융정보 제3자 제공 동의에 체크해주세요.")
    private Boolean financialInfoThirdPartyProvisionConsent;

    @NotNull(message = "개인정보 제3자 제공 동의(오픈 뱅킹)에 체크해주세요.")
    private Boolean openBankingPersonalInfoThirdPartyProvisionConsent;

    @NotNull(message = "본인인증,전자서명을 위한 개인정보 제3자 제공 동의에 체크해주세요.")
    private Boolean personalInfoThirdPartyConsentForESigniture;

    @Builder(access = AccessLevel.PRIVATE)
    private SignUpRequest(String name, String email, String phoneNumber, String birthDay, Gender gender, String pinNumber, Boolean consentAbove14, Boolean serviceTermsConsent, Boolean consentPersonalInfo, Boolean adInfoConsent, Boolean myDataConsentPersonalInfo, String bank, String bankAccountNumber, Boolean openBankingAutoTransferConsent, Boolean openBankingFinancialInfoInquiryConsent, Boolean financialInfoThirdPartyProvisionConsent, Boolean openBankingPersonalInfoThirdPartyProvisionConsent, Boolean personalInfoThirdPartyConsentForESigniture) {
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
        this.bank = bank;
        this.bankAccountNumber = bankAccountNumber;
        this.openBankingAutoTransferConsent = openBankingAutoTransferConsent;
        this.openBankingFinancialInfoInquiryConsent = openBankingFinancialInfoInquiryConsent;
        this.financialInfoThirdPartyProvisionConsent = financialInfoThirdPartyProvisionConsent;
        this.openBankingPersonalInfoThirdPartyProvisionConsent = openBankingPersonalInfoThirdPartyProvisionConsent;
        this.personalInfoThirdPartyConsentForESigniture = personalInfoThirdPartyConsentForESigniture;
    }

    public static SignUpRequest of(String name, String email, String phoneNumber, String birthDay, Gender gender, String pinNumber, String bank, String bankAccountNumber, Boolean consentAbove14, Boolean serviceTermsConsent, Boolean consentPersonalInfo, Boolean adInfoConsent, Boolean myDataConsentPersonalInfo, Boolean openBankingAutoTransferConsent, Boolean openBankingFinancialInfoInquiryConsent, Boolean financialInfoThirdPartyProvisionConsent, Boolean openBankingPersonalInfoThirdPartyProvisionConsent, Boolean personalInfoThirdPartyConsentForESigniture) {
        return SignUpRequest.builder()
                .name(name)
                .email(email)
                .phoneNumber(phoneNumber)
                .birthDay(birthDay)
                .gender(gender)
                .pinNumber(pinNumber)
                .bank(bank)
                .bankAccountNumber(bankAccountNumber)
                .consentAbove14(consentAbove14)
                .serviceTermsConsent(serviceTermsConsent)
                .consentPersonalInfo(consentPersonalInfo)
                .adInfoConsent(resolveSelectableConsent(adInfoConsent))
                .myDataConsentPersonalInfo(resolveSelectableConsent(myDataConsentPersonalInfo))
                .openBankingAutoTransferConsent(openBankingAutoTransferConsent)
                .openBankingFinancialInfoInquiryConsent(openBankingFinancialInfoInquiryConsent)
                .financialInfoThirdPartyProvisionConsent(financialInfoThirdPartyProvisionConsent)
                .openBankingPersonalInfoThirdPartyProvisionConsent(openBankingPersonalInfoThirdPartyProvisionConsent)
                .personalInfoThirdPartyConsentForESigniture(personalInfoThirdPartyConsentForESigniture)
                .build();
    }

    public LocalDate getBirthDay() {
        int year = Integer.parseInt(birthDay.substring(0, 2));
        int month = Integer.parseInt(birthDay.substring(2, 4));
        int day = Integer.parseInt(birthDay.substring(4, 6));

        LocalDate over2000 = LocalDate.of(2000 + year, month, day);
        LocalDate over1900 = LocalDate.of(1900 + year, month, day);

        LocalDate now = LocalDate.now();
        Period between2000AndNow = Period.between(over2000, now);
        Period between1900AndNow = Period.between(over1900, now);

        if (Math.abs(between2000AndNow.getYears()) < Math.abs(between1900AndNow.getYears())) {
            return over2000;
        }
        return over1900;
    }

    public Boolean getAdInfoConsent() {
        return resolveSelectableConsent(adInfoConsent);
    }

    public Boolean getMyDataConsentPersonalInfo() {
        return resolveSelectableConsent(myDataConsentPersonalInfo);
    }

    private static Boolean resolveSelectableConsent(Boolean consent) {
        return consent == null ? Boolean.FALSE : consent;
    }
}