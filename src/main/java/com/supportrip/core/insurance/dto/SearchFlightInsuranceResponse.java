package com.supportrip.core.insurance.dto;

import com.supportrip.core.insurance.domain.FlightInsurance;
import lombok.*;

import java.util.List;

@Getter
public class SearchFlightInsuranceResponse {
    private Long id;
    private String insuranceName;
    private int premium;
    private int minJoinAge;
    private int maxJoinAge;
    private String planName;
    private String companyName;
    private String logoImageUrl;
    private List<Top3SpecialContractResponse> specialContracts;

    @Builder(access = AccessLevel.PRIVATE)
    public SearchFlightInsuranceResponse(Long id, String insuranceName, int premium, int minJoinAge, int maxJoinAge, String planName, String companyName, String logoImageUrl, List<Top3SpecialContractResponse> specialContracts) {
        this.id = id;
        this.insuranceName = insuranceName;
        this.premium = premium;
        this.minJoinAge = minJoinAge;
        this.maxJoinAge = maxJoinAge;
        this.planName = planName;
        this.companyName = companyName;
        this.logoImageUrl = logoImageUrl;
        this.specialContracts = specialContracts;
    }

    public static SearchFlightInsuranceResponse toDTO(FlightInsurance flightInsurance, List<Top3SpecialContractResponse> specialContracts) {
        SearchFlightInsuranceResponse response = SearchFlightInsuranceResponse.builder()
                .id(flightInsurance.getId())
                .insuranceName(flightInsurance.getName())
                .premium(flightInsurance.getPremium())
                .minJoinAge(flightInsurance.getMinJoinAge())
                .maxJoinAge(flightInsurance.getMaxJoinAge())
                .planName(flightInsurance.getPlanName())
                .companyName(flightInsurance.getInsuranceCompany().getName())
                .logoImageUrl(flightInsurance.getInsuranceCompany().getLogoImageUrl())
                .specialContracts(specialContracts)
                .build();
        return response;
    }


}
