package com.supportrip.core.insurance.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TravelInsurance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "insurance_company_id")
    private InsuranceCompany insuranceCompany;

    private String name;

    private int premium;

    private int minJoinAge;

    private int maxJoinAge;

    private boolean overseasMedicalExpenses;

    private boolean phoneLoss;

}

