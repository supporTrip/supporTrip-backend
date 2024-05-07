package com.supportrip.core.system.core.insurance.internal.presentation.request;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import java.beans.ConstructorProperties;
import java.time.LocalDateTime;

@Getter
public class SubscriptionRequest {

    private Long flightInsuranceId;

    @NotNull(message = "보장시작일이 존재하지 않습니다.")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime coverageStartAt;

    @NotNull(message = "보장종료일이 존재하지 않습니다.")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime coverageEndAt;

    @NotNull(message = "보험료가 존재하지 않습니다.")
    private int totalPremium;

    @AssertTrue(message = "가입하는 보험의 보장내용, 보험약관, 주요내용확인 여부를 확인해 주세요.")
    @NotNull(message = "가입하는 보험의 보장내용, 보험약관, 주요내용확인 여부가 존재하지 않습니다.")
    private Boolean coverageDetailsTermsContent;

    @AssertTrue(message = "개인정보 수집 및 이용 동의를 확인해 주세요")
    @NotNull(message = "개인정보 수집 및 이용 동의 여부가 존재하지 않습니다.")
    private Boolean consentPersonalInfo;

    @ConstructorProperties({"flightInsuranceId", "coverageStartAt", "coverageEndAt", "totalPremium", "coverageDetailsTermsContent", "consentPersonalInfo"})
    @Builder(access = AccessLevel.PRIVATE)
    private SubscriptionRequest(Long flightInsuranceId, LocalDateTime coverageStartAt, LocalDateTime coverageEndAt, int totalPremium, Boolean coverageDetailsTermsContent, Boolean consentPersonalInfo) {
        this.flightInsuranceId = flightInsuranceId;
        this.coverageStartAt = coverageStartAt;
        this.coverageEndAt = coverageEndAt;
        this.totalPremium = totalPremium;
        this.coverageDetailsTermsContent = coverageDetailsTermsContent;
        this.consentPersonalInfo = consentPersonalInfo;
    }

    public static SubscriptionRequest of(Long flightInsuranceId, LocalDateTime coverageStartAt, LocalDateTime coverageEndAt, int totalPremium, Boolean coverageDetailsTermsContent, Boolean consentPersonalInfo) {
        return SubscriptionRequest.builder()
                .flightInsuranceId(flightInsuranceId)
                .coverageStartAt(coverageStartAt)
                .coverageEndAt(coverageEndAt)
                .totalPremium(totalPremium)
                .coverageDetailsTermsContent(coverageDetailsTermsContent)
                .consentPersonalInfo(consentPersonalInfo)
                .build();
    }
}
