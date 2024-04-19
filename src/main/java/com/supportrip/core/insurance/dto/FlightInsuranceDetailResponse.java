package com.supportrip.core.insurance.dto;

import com.supportrip.core.insurance.domain.FlightInsurance;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class FlightInsuranceDetailResponse {
    private String companyName;
    private String logoImageUrl;
    private String insuranceName;
    private LocalDateTime coverageStartAt;
    private LocalDateTime coverageEndAt;
    private int premium;
    private String planName;
    private List<SpecialContractResponse> specialContracts;

    @Builder(access = AccessLevel.PRIVATE)
    private FlightInsuranceDetailResponse(String companyName, String logoImageUrl, String insuranceName, LocalDateTime coverageStartAt, LocalDateTime coverageEndAt, int premium, String planName, List<SpecialContractResponse> specialContracts) {
        this.companyName = companyName;
        this.logoImageUrl = logoImageUrl;
        this.insuranceName = insuranceName;
        this.coverageStartAt = coverageStartAt;
        this.coverageEndAt = coverageEndAt;
        this.premium = premium;
        this.planName = planName;
        this.specialContracts = specialContracts;
    }

    public static FlightInsuranceDetailResponse toDTO(FlightInsurance flightInsurance, LocalDateTime coverageStartAt, LocalDateTime coverageEndAt, int premium, String planName, List<SpecialContractResponse> specialContracts) {
       return FlightInsuranceDetailResponse.builder()
                .companyName(flightInsurance.getInsuranceCompany().getName())
                .logoImageUrl(flightInsurance.getInsuranceCompany().getLogoImageUrl())
                .insuranceName(flightInsurance.getName())
                .coverageStartAt(coverageStartAt)
                .coverageEndAt(coverageEndAt)
                .premium(premium)
                .planName(planName)
                .specialContracts(specialContracts)
                .build();
    }
}
