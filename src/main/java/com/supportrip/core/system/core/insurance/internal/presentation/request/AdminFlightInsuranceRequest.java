package com.supportrip.core.system.core.insurance.internal.presentation.request;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class AdminFlightInsuranceRequest {
    private Long id;

    @NotNull(message = "보험상품 이름을 입력해주세요.")
    private String name;

    @NotNull(message = "보험상품 가격을 입력해주세요.")
    private int premium;

    @NotNull(message = "최소 가입 연령을 입력해주세요.")
    private int minAge;

    @NotNull(message = "최대 가입 연령을 입력해주세요.")
    private int maxAge;

    @NotNull(message = "항공기지연 보장 여부를 입력해주세요.")
    private Boolean flightDelay;

    @NotNull(message = "여권분실 보장 여부를 입력해주세요.")
    private Boolean passportLoss;

    @NotNull(message = "식중독 보장 여부를 입력해주세요.")
    private Boolean foodPoisoning;

    @NotNull(message = "보험사 정보를 입력해주세요.")
    private AdminInsuranceCompanyRequest insuranceCompany;

    @NotNull(message = "특약 정보를 입력해 주세요.")
    private List<AdminSpecialContractsRequest> specialContracts;

    @Builder(access = AccessLevel.PRIVATE)
    private AdminFlightInsuranceRequest(Long id, String name, int premium, int minAge, int maxAge, Boolean flightDelay, Boolean passportLoss, Boolean foodPoisoning, AdminInsuranceCompanyRequest insuranceCompany, List<AdminSpecialContractsRequest> specialContracts) {
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
}
