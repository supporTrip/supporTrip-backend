package com.supportrip.core.insurance.dto;

import com.supportrip.core.insurance.domain.FlightInsurance;
import com.supportrip.core.insurance.domain.InsuranceCompany;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class AdminFlightInsuranceResponse {
    private Long id;
    private String name;
    private int premium;
    private int minAge;
    private int maxAge;
    private InsuranceCompany insuranceCompany;
    private List<SpecialContractResponse> specialContractResponses;

    @Builder(access = AccessLevel.PRIVATE)
    private AdminFlightInsuranceResponse(Long id, String name, int premium, int minAge, int maxAge, InsuranceCompany insuranceCompany, List<SpecialContractResponse> specialContractResponses) {
        this.id = id;
        this.name = name;
        this.premium = premium;
        this.minAge = minAge;
        this.maxAge = maxAge;
        this.insuranceCompany = insuranceCompany;
        this.specialContractResponses = specialContractResponses;
    }

    public static AdminFlightInsuranceResponse of (FlightInsurance flightInsurance, List<SpecialContractResponse> specialContractResponses) {
        return AdminFlightInsuranceResponse.builder()
                .id(flightInsurance.getId())
                .name(flightInsurance.getName())
                .premium(flightInsurance.getPremium())
                .minAge(flightInsurance.getMinJoinAge())
                .maxAge(flightInsurance.getMaxJoinAge())
                .insuranceCompany(flightInsurance.getInsuranceCompany())
                .specialContractResponses(specialContractResponses)
                .build();
    }
}
