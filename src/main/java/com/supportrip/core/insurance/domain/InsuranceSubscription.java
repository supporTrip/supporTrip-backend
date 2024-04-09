package com.supportrip.core.insurance.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
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
    @Column(name = "insurance_subscription_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "flight_insurance_id")
    private FlightInsurance flightInsurance;

//    private Users users;

    @NotNull
    private LocalDateTime coverageStartAt;

    @NotNull
    private LocalDateTime coverageEndAt;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}