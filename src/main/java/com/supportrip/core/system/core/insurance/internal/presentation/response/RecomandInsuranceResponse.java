package com.supportrip.core.system.core.insurance.internal.presentation.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
public class RecomandInsuranceResponse {
    private final String companyName;
    private final String insuranceName;
    private final int premium;
    @Builder(access = AccessLevel.PRIVATE)
    private RecomandInsuranceResponse(String companyName, String insuranceName, int premium) {
        this.companyName = companyName;
        this.insuranceName = insuranceName;
        this.premium = premium;
    }




    public static RecomandInsuranceResponse of(String companyName, String insuranceName, int premium){
        return RecomandInsuranceResponse.builder()
                .companyName(companyName)
                .insuranceName(insuranceName)
                .premium(premium)
                .build();
    }
}
