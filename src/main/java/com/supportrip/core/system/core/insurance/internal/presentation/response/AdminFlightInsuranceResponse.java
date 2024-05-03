package com.supportrip.core.system.core.insurance.internal.presentation.response;

import com.supportrip.core.system.core.insurance.internal.domain.FlightInsurance;
import com.supportrip.core.system.core.insurance.internal.domain.InsuranceCompany;
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
    private boolean flightDelay;
    private boolean passportLoss;
    private boolean foodPoisoning;
    private InsuranceCompany insuranceCompany;
    private List<SpecialContractResponse> specialContracts;

    @Builder(access = AccessLevel.PRIVATE)
    private AdminFlightInsuranceResponse(Long id, String name, int premium, int minAge, int maxAge, boolean flightDelay, boolean passportLoss, boolean foodPoisoning, InsuranceCompany insuranceCompany, List<SpecialContractResponse> specialContracts) {
        this.id = id;
        this.name = name;
        this.premium = premium;
        this.minAge = minAge;
        this.maxAge = maxAge;
        this.flightDelay = flightDelay;
        this.passportLoss = passportLoss;
        this.foodPoisoning = foodPoisoning;
        this.insuranceCompany = insuranceCompany;
        this.specialContracts = specialContracts;
    }

    public static AdminFlightInsuranceResponse of (FlightInsurance flightInsurance, List<SpecialContractResponse> specialContracts) {
        return AdminFlightInsuranceResponse.builder()
                .id(flightInsurance.getId())
                .name(flightInsurance.getName())
                .premium(flightInsurance.getPremium())
                .minAge(flightInsurance.getMinJoinAge())
                .maxAge(flightInsurance.getMaxJoinAge())
                .flightDelay(flightInsurance.isFlightDelay())
                .passportLoss(flightInsurance.isPassportLoss())
                .foodPoisoning(flightInsurance.isFoodPoisoning())
                .insuranceCompany(flightInsurance.getInsuranceCompany())
                .specialContracts(specialContracts)
                .build();
    }
}
