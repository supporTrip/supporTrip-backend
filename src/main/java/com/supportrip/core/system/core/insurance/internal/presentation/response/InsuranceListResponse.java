package com.supportrip.core.system.core.insurance.internal.presentation.response;

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
