package com.supportrip.core.system.core.insurance.internal.domain;

import com.supportrip.core.system.common.internal.BaseEntity;
import com.supportrip.core.system.core.user.internal.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
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

    @Builder(access = AccessLevel.PRIVATE)
    private InsuranceSubscription(Long id, int totalPremium, FlightInsurance flightInsurance, User user, LocalDateTime coverageStartAt, LocalDateTime coverageEndAt, boolean coverageDetailsTermsContent, boolean consentPersonalInfo) {
        this.id = id;
        this.totalPremium = totalPremium;
        this.flightInsurance = flightInsurance;
        this.user = user;
        this.coverageStartAt = coverageStartAt;
        this.coverageEndAt = coverageEndAt;
        this.coverageDetailsTermsContent = coverageDetailsTermsContent;
        this.consentPersonalInfo = consentPersonalInfo;
    }

    public static InsuranceSubscription createInsuranceSubscription(User user, FlightInsurance flightInsurance, int totalPremium, LocalDateTime coverageStartAt, LocalDateTime coverageEndAt, boolean coverageDetailsTermsContent, boolean consentPersonalInfo) {
        return InsuranceSubscription.builder()
                .user(user)
                .flightInsurance(flightInsurance)
                .totalPremium(totalPremium)
                .coverageStartAt(coverageStartAt)
                .coverageEndAt(coverageEndAt)
                .coverageDetailsTermsContent(coverageDetailsTermsContent)
                .consentPersonalInfo(consentPersonalInfo)
                .build();
    }
}