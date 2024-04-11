package com.supportrip.core.insurance.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "flight_insurance")
public class FlightInsurance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "flight_insurance_id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "insurance_company_id")
    private InsuranceCompany insuranceCompany;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "premium", nullable = false)
    private int premium;

    @Column(name = "min_join_age", nullable = false)
    private int minJoinAge;

    @Column(name = "max_join_age", nullable = false)
    private int maxJoinAge;

    @Column(name = "plan_name", nullable = false)
    private String planName;

    @Column(name = "overseas_medical_expenses", nullable = false)
    private boolean overseasMedicalExpenses;

    @Column(name = "phone_loss", nullable = false)
    private boolean phoneLoss;

    @Column(name = "flight_delay", nullable = false)
    private boolean flightDelay;

    @Column(name = "passport_loss", nullable = false)
    private boolean passportLoss;

    @Column(name = "food_poisoning", nullable = false)
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

