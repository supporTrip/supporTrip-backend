package com.supportrip.core.insurance.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Getter
public class SubscriptionRequest {

    private Long flightInsuranceId;

    @NotNull(message = "보장시작일이 존재하지 않습니다.")
    private LocalDateTime coverageStartAt;

    @NotNull(message = "보장종료일이 존재하지 않습니다.")
    private LocalDateTime coverageEndAt;

    @NotNull(message = "보험료가 존재하지 않습니다.")
    private int totalPremium;

    @NotNull(message = "가입하는 보험의 보장내용, 보험약관, 주요내용확인 동의를 체크해 주세요.")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private Boolean coverageDetailsTermsContent;

    @NotNull(message = "개인정보 수집 및 이용 동의를 체크해 주세요")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private Boolean consentPersonalInfo;

    @Builder(access = AccessLevel.PRIVATE)
    private SubscriptionRequest(Long flightInsuranceId, LocalDateTime coverageStartAt, LocalDateTime coverageEndAt, int totalPremium, Boolean coverageDetailsTermsContent, Boolean consentPersonalInfo) {
        this.flightInsuranceId = flightInsuranceId;
        this.coverageStartAt = coverageStartAt;
        this.coverageEndAt = coverageEndAt;
        this.totalPremium = totalPremium;
        this.coverageDetailsTermsContent = coverageDetailsTermsContent;
        this.consentPersonalInfo = consentPersonalInfo;
    }
}
