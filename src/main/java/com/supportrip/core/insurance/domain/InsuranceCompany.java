package com.supportrip.core.insurance.domain;

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
    private String InsuranceCompanyUrl;

    @Builder
    private InsuranceCompany(Long id, String name, String logoImageUrl, String insuranceCompanyUrl) {
        this.id = id;
        this.name = name;
        this.logoImageUrl = logoImageUrl;
        InsuranceCompanyUrl = insuranceCompanyUrl;
    }

    //==정적 팩토리 메서드==//
    public static InsuranceCompany from(String name) {
        return builder()
                .name(name)
                .build();
    }
}