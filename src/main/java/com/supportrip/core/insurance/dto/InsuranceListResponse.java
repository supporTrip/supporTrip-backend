package com.supportrip.core.insurance.dto;

import lombok.*;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InsuranceListResponse {
    private List<InsuranceResponse> insuranceList;

    public static InsuranceListResponse of(List<InsuranceResponse> insuranceResponses){
        return InsuranceListResponse.builder()
                .insuranceList(insuranceResponses)
                .build();
    }

}
