package com.supportrip.core.system.core.insurance.internal.presentation.response;

import com.supportrip.core.system.core.insurance.internal.domain.FlightInsurance;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class FilterAndCalPremiumResponse {
    private Long id;
    private String insuranceName;
    private int premium;
    private String planName;
    private String companyName;
    private String logoImageUrl;
    private LocalDateTime departAt;
    private LocalDateTime arrivalAt;

    @Builder(access = AccessLevel.PRIVATE)
    private FilterAndCalPremiumResponse(Long id, String insuranceName, int premium, String planName, String companyName, String logoImageUrl, LocalDateTime departAt, LocalDateTime arrivalAt) {
        this.id = id;
        this.insuranceName = insuranceName;
        this.premium = premium;
        this.planName = planName;
        this.companyName = companyName;
        this.logoImageUrl = logoImageUrl;
        this.departAt = departAt;
        this.arrivalAt = arrivalAt;
    }

    public static FilterAndCalPremiumResponse of(FlightInsurance flightInsurance, int calPremium, LocalDateTime departAt, LocalDateTime arrivalAt) {
        return FilterAndCalPremiumResponse.builder()
                .id(flightInsurance.getId())
                .insuranceName(flightInsurance.getName())
                .premium(calPremium)
                .planName(flightInsurance.getName())
                .companyName(flightInsurance.getInsuranceCompany().getName())
                .logoImageUrl(flightInsurance.getInsuranceCompany().getLogoImageUrl())
                .departAt(departAt)
                .arrivalAt(arrivalAt)
                .build();
    }
}
