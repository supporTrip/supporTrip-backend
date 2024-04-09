package com.supportrip.core.insurance.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class InsuranceCompany {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "insurance_company_id")
    private Long id;

    @NotNull
    private String name;

    @NotNull
    private String logoImageUrl;

    @NotNull
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