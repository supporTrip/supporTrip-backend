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
public class InsuranceSubscription extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "total_premium")
    private int totalPremium;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "flight_insurance_id")
    private FlightInsurance flightInsurance;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "coverage_start_at", nullable = false)
    private LocalDateTime coverageStartAt;

    @Column(name = "coverage_end_at", nullable = false)
    private LocalDateTime coverageEndAt;

    @Column(name = "coverage_details_terms_content", nullable = false)
    private boolean coverageDetailsTermsContent;

    @Column(name = "consent_personal_info", nullable = false)
    private boolean consentPersonalInfo;

    private LocalDateTime updatedAt;
}