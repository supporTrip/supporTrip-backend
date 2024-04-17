package com.supportrip.core.user.domain;

import com.supportrip.core.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "user_consent_status")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserConsentStatus extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "consent_above_14", nullable = false)
    private boolean consentAbove14;

    @Column(name = "service_terms_consent", nullable = false)
    private boolean serviceTermsConsent;

    @Column(name = "consent_personal_info", nullable = false)
    private boolean consentPersonalInfo;

    @Column(name = "ad_info_consent", nullable = false)
    private boolean adInfoConsent;

    @Column(name = "mydata_consent_personal_info", nullable = false)
    private boolean myDataConsentPersonalInfo;

    @Builder(access = AccessLevel.PRIVATE)
    private UserConsentStatus(Long id, User user, boolean consentAbove14, boolean serviceTermsConsent, boolean consentPersonalInfo, boolean adInfoConsent, boolean myDataConsentPersonalInfo) {
        this.id = id;
        this.user = user;
        this.consentAbove14 = consentAbove14;
        this.serviceTermsConsent = serviceTermsConsent;
        this.consentPersonalInfo = consentPersonalInfo;
        this.adInfoConsent = adInfoConsent;
        this.myDataConsentPersonalInfo = myDataConsentPersonalInfo;
    }

    public static UserConsentStatus of(User user,
                                       boolean consentAbove14,
                                       boolean serviceTermsConsent,
                                       boolean consentPersonalInfo,
                                       boolean adInfoConsent,
                                       boolean myDataConsentPersonalInfo) {
        return UserConsentStatus.builder()
                .user(user)
                .consentAbove14(consentAbove14)
                .serviceTermsConsent(serviceTermsConsent)
                .consentPersonalInfo(consentPersonalInfo)
                .adInfoConsent(adInfoConsent)
                .myDataConsentPersonalInfo(myDataConsentPersonalInfo)
                .build();
    }
}