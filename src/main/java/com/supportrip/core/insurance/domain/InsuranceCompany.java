package com.supportrip.core.insurance.domain;

import com.supportrip.core.insurance.dto.AdminInsuranceCompanyRequest;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "insurance_company")
public class InsuranceCompany {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "logo_image_url", nullable = false)
    private String logoImageUrl;

    @Column(name = "insurance_company_url", nullable = false)
    private String insuranceCompanyUrl;

    @Builder(access = AccessLevel.PRIVATE)
    private InsuranceCompany(Long id, String name, String logoImageUrl, String insuranceCompanyUrl) {
        this.id = id;
        this.name = name;
        this.logoImageUrl = logoImageUrl;
        this.insuranceCompanyUrl = insuranceCompanyUrl;
    }

    public static InsuranceCompany from(String name) {
        return builder()
                .name(name)
                .build();
    }

    public static InsuranceCompany create(String name, String logoImageUrl, String insuranceCompanyUrl) {
        return InsuranceCompany.builder()
                .name(name)
                .logoImageUrl(logoImageUrl)
                .insuranceCompanyUrl(insuranceCompanyUrl)
                .build();
    }

    public void update(AdminInsuranceCompanyRequest insuranceCompany) {
        this.name = insuranceCompany.getName();
        this.logoImageUrl = insuranceCompany.getLogoImageUrl();
        this.insuranceCompanyUrl = insuranceCompany.getInsuranceCompanyUrl();
    }
}