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
    @Column(name = "id")
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

    @Column(name = "flight_delay", nullable = false)
    private boolean flightDelay;

    @Column(name = "passport_loss", nullable = false)
    private boolean passportLoss;

    @Column(name = "food_poisoning", nullable = false)
    private boolean foodPoisoning;

    public void setPremium(int premium) {
        this.premium = premium;
    }

    @Builder(access = AccessLevel.PRIVATE)
    private FlightInsurance(Long id, InsuranceCompany insuranceCompany, String name, int premium, int minJoinAge, int maxJoinAge, boolean flightDelay, boolean passportLoss, boolean foodPoisoning) {
        this.id = id;
        this.insuranceCompany = insuranceCompany;
        this.name = name;
        this.premium = premium;
        this.minJoinAge = minJoinAge;
        this.maxJoinAge = maxJoinAge;
        this.flightDelay = flightDelay;
        this.passportLoss = passportLoss;
        this.foodPoisoning = foodPoisoning;
    }

    public static FlightInsurance of(InsuranceCompany insuranceCompany, String name, int premium, int minJoinAge, int maxJoinAge, boolean flightDelay, boolean passportLoss, boolean foodPoisoning) {
        return builder()
                .insuranceCompany(insuranceCompany)
                .name(name)
                .premium(premium)
                .minJoinAge(minJoinAge)
                .maxJoinAge(maxJoinAge)
                .flightDelay(flightDelay)
                .passportLoss(passportLoss)
                .foodPoisoning(foodPoisoning)
                .build();
    }

    public static FlightInsurance create(String name, int premium, int minAge, int maxAge, Boolean flightDelay, Boolean passportLoss, Boolean foodPoisoning, InsuranceCompany insuranceCompany) {
        return FlightInsurance.builder()
                .name(name)
                .premium(premium)
                .minJoinAge(minAge)
                .maxJoinAge(maxAge)
                .flightDelay(flightDelay)
                .passportLoss(passportLoss)
                .foodPoisoning(foodPoisoning)
                .insuranceCompany(insuranceCompany)
                .build();
    }

    public void update(String name, int premium, int minAge, int maxAge, Boolean flightDelay, Boolean passportLoss, Boolean foodPoisoning, InsuranceCompany insuranceCompany) {
        this.name = name;
        this.premium = premium;
        this.minJoinAge = minAge;
        this.maxJoinAge = maxAge;
        this.flightDelay = flightDelay;
        this.passportLoss = passportLoss;
        this.foodPoisoning = foodPoisoning;
        this.insuranceCompany = insuranceCompany;
    }

}

