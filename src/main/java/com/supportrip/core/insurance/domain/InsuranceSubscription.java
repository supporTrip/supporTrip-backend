package com.supportrip.core.insurance.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class InsuranceSubscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "travel_insurance_id")
    private TravelInsurance travelInsurance;

//    private Users users;

    private LocalDateTime coverageStartAt;

    private LocalDateTime coverageEndAt;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}