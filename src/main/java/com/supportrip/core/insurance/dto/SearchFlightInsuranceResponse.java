package com.supportrip.core.insurance.dto;

import com.supportrip.core.insurance.domain.FlightInsurance;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class SearchFlightInsuranceResponse {
    private Long id;
    private String insuranceName;
    private int premium;
    private String planName;
    private String companyName;
    private String logoImageUrl;
    private LocalDateTime departAt;
    private LocalDateTime arrivalAt;
    private List<Top3SpecialContractResponse> specialContracts;

    @Builder(access = AccessLevel.PRIVATE)
    public SearchFlightInsuranceResponse(Long id, String insuranceName, int premium, String planName, String companyName, String logoImageUrl, List<Top3SpecialContractResponse> specialContracts, LocalDateTime departAt, LocalDateTime arrivalAt) {
        this.id = id;
        this.insuranceName = insuranceName;
        this.premium = premium;
        this.planName = planName;
        this.companyName = companyName;
        this.logoImageUrl = logoImageUrl;
        this.departAt = departAt;
        this.arrivalAt = arrivalAt;
        this.specialContracts = specialContracts;
    }

    public static SearchFlightInsuranceResponse toDTO(FilterAndCalPremiumResponse flightInsurance, List<Top3SpecialContractResponse> specialContracts, String planName, LocalDateTime departAt, LocalDateTime arrivalAt) {
        SearchFlightInsuranceResponse response = SearchFlightInsuranceResponse.builder()
                .id(flightInsurance.getId())
                .insuranceName(flightInsurance.getCompanyName())
                .premium(flightInsurance.getPremium())
                .planName(planName)
                .companyName(flightInsurance.getCompanyName())
                .logoImageUrl(flightInsurance.getLogoImageUrl())
                .departAt(departAt)
                .arrivalAt(arrivalAt)
                .specialContracts(specialContracts)
                .build();
        return response;
    }


}
