package com.supportrip.core.insurance.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FlightInsurance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "flight_insurance_id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "insurance_company_id")
    private InsuranceCompany insuranceCompany;

    @NotNull
    private String name;

    @NotNull
    private int premium;

    @NotNull
    private int minJoinAge;

    @NotNull
    private int maxJoinAge;

    @NotNull
    private String planName;

    @NotNull
    private boolean overseasMedicalExpenses;

    @NotNull
    private boolean phoneLoss;

    @NotNull
    private boolean flightDelay;

    @NotNull
    private boolean passportLoss;

    @NotNull
    private boolean foodPoisoning;

    public void setPremium(int premium) {
        this.premium = premium;
    }

    @Builder
    private FlightInsurance(Long id, InsuranceCompany insuranceCompany, String name, int premium, int minJoinAge, int maxJoinAge, String planName, boolean overseasMedicalExpenses, boolean phoneLoss, boolean flightDelay, boolean passportLoss, boolean foodPoisoning) {
        this.id = id;
        this.insuranceCompany = insuranceCompany;
        this.name = name;
        this.premium = premium;
        this.minJoinAge = minJoinAge;
        this.maxJoinAge = maxJoinAge;
        this.planName = planName;
        this.overseasMedicalExpenses = overseasMedicalExpenses;
        this.phoneLoss = phoneLoss;
        this.flightDelay = flightDelay;
        this.passportLoss = passportLoss;
        this.foodPoisoning = foodPoisoning;
    }

    //==정적 팩토리 메서드==//
    public static FlightInsurance of(Long id, InsuranceCompany insuranceCompany, String name, int premium, int minJoinAge, int maxJoinAge, String planName, boolean overseasMedicalExpenses, boolean phoneLoss, boolean flightDelay, boolean passportLoss, boolean foodPoisoning) {
        return builder()
                .id(id)
                .insuranceCompany(insuranceCompany)
                .name(name)
                .premium(premium)
                .minJoinAge(minJoinAge)
                .maxJoinAge(maxJoinAge)
                .planName(planName)
                .overseasMedicalExpenses(overseasMedicalExpenses)
                .phoneLoss(phoneLoss)
                .flightDelay(flightDelay)
                .passportLoss(passportLoss)
                .foodPoisoning(foodPoisoning)
                .build();
    }

}

