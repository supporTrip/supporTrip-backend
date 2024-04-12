package com.supportrip.core.insurance.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "insurance_subscription")
public class InsuranceSubscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "flight_insurance_id")
    private FlightInsurance flightInsurance;

//    private Users users;

    @Column(name = "coverage_start_at", nullable = false)
    private LocalDateTime coverageStartAt;

    @Column(name = "coverage_end_at", nullable = false)
    private LocalDateTime coverageEndAt;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}