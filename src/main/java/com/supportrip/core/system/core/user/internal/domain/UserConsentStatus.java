package com.supportrip.core.system.core.user.internal.domain;

import com.supportrip.core.system.common.internal.BaseEntity;
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

    @Column(name = "open_banking_auto_transfer_consent", nullable = false)
    private boolean openBankingAutoTransferConsent;

    @Column(name = "open_banking_financial_info_inquiry_consent", nullable = false)
    private boolean openBankingFinancialInfoInquiryConsent;

    @Column(name = "financial_info_third_party_provision_consent", nullable = false)
    private boolean financialInfoThirdPartyProvisionConsent;

    @Column(name = "open_banking_personal_info_third_party_provision_consent", nullable = false)
    private boolean openBankingPersonalInfoThirdPartyProvisionConsent;

    @Column(name = "personal_info_third_party_consent_for_e_signature", nullable = false)
    private boolean personalInfoThirdPartyConsentForESigniture;

    @Builder(access = AccessLevel.PRIVATE)
    private UserConsentStatus(Long id, User user, boolean consentAbove14, boolean serviceTermsConsent, boolean consentPersonalInfo, boolean adInfoConsent, boolean myDataConsentPersonalInfo, boolean openBankingAutoTransferConsent, boolean openBankingFinancialInfoInquiryConsent, boolean financialInfoThirdPartyProvisionConsent, boolean openBankingPersonalInfoThirdPartyProvisionConsent, boolean personalInfoThirdPartyConsentForESigniture) {
        this.id = id;
        this.user = user;
        this.consentAbove14 = consentAbove14;
        this.serviceTermsConsent = serviceTermsConsent;
        this.consentPersonalInfo = consentPersonalInfo;
        this.adInfoConsent = adInfoConsent;
        this.myDataConsentPersonalInfo = myDataConsentPersonalInfo;
        this.openBankingAutoTransferConsent = openBankingAutoTransferConsent;
        this.openBankingFinancialInfoInquiryConsent = openBankingFinancialInfoInquiryConsent;
        this.financialInfoThirdPartyProvisionConsent = financialInfoThirdPartyProvisionConsent;
        this.openBankingPersonalInfoThirdPartyProvisionConsent = openBankingPersonalInfoThirdPartyProvisionConsent;
        this.personalInfoThirdPartyConsentForESigniture = personalInfoThirdPartyConsentForESigniture;
    }

    public static UserConsentStatus of(User user,
                                       boolean consentAbove14,
                                       boolean serviceTermsConsent,
                                       boolean consentPersonalInfo,
                                       boolean adInfoConsent,
                                       boolean myDataConsentPersonalInfo,
                                       boolean openBankingAutoTransferConsent,
                                       boolean openBankingFinancialInfoInquiryConsent,
                                       boolean financialInfoThirdPartyProvisionConsent,
                                       boolean openBankingPersonalInfoThirdPartyProvisionConsent,
                                       boolean personalInfoThirdPartyConsentForESigniture) {
        return UserConsentStatus.builder()
                .user(user)
                .consentAbove14(consentAbove14)
                .serviceTermsConsent(serviceTermsConsent)
                .consentPersonalInfo(consentPersonalInfo)
                .adInfoConsent(adInfoConsent)
                .myDataConsentPersonalInfo(myDataConsentPersonalInfo)
                .openBankingAutoTransferConsent(openBankingAutoTransferConsent)
                .openBankingFinancialInfoInquiryConsent(openBankingFinancialInfoInquiryConsent)
                .financialInfoThirdPartyProvisionConsent(financialInfoThirdPartyProvisionConsent)
                .openBankingPersonalInfoThirdPartyProvisionConsent(openBankingPersonalInfoThirdPartyProvisionConsent)
                .personalInfoThirdPartyConsentForESigniture(personalInfoThirdPartyConsentForESigniture)
                .build();
    }
}