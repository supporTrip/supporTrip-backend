package com.supportrip.core.system.core.insurance.internal.presentation.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class RecomandInsuranceListResponse {
    private List<RecomandInsuranceResponse> insuranceList;

    @Builder(access = AccessLevel.PRIVATE)
    private RecomandInsuranceListResponse(List<RecomandInsuranceResponse> insuranceList) {
        this.insuranceList = insuranceList;
    }

    public static RecomandInsuranceListResponse of(List<RecomandInsuranceResponse> insuranceList){
        return RecomandInsuranceListResponse.builder()
                .insuranceList(insuranceList)
                .build();
    }
}
