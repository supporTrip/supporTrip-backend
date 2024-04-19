package com.supportrip.core.insurance.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Getter
public class FlightInsuranceDetailRequest {
    @NotNull(message = "보험료가 존재하지 않습니다.")
    private int premium;

    @NotNull(message = "플랜이 입력되지 않았습니다.")
    private String planName;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @NotNull(message = "가입 시작 날짜가 입력되지 않았습니다.")
    private LocalDateTime coverageStartAt;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @NotNull(message = "가입 종료 날짜를 입력되지 않았습니다.")
    private LocalDateTime coverageEndAt;

    @Builder(access = AccessLevel.PRIVATE)
    private FlightInsuranceDetailRequest(int premium, String planName, LocalDateTime coverageStartAt, LocalDateTime coverageEndAt) {
        this.premium = premium;
        this.planName = planName;
        this.coverageStartAt = coverageStartAt;
        this.coverageEndAt = coverageEndAt;
    }
}
