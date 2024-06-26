package com.supportrip.core.system.core.insurance.internal.presentation.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class AdminInsuranceCompanyRequest {
    private Long id;

    @NotNull(message = "보험사 이름을 입력해주세요.")
    private String name;

    @NotNull(message = "보험사 이미지 URL을 입력해주세요.")
    private String logoImageUrl;

    @NotNull(message = "보험사 URL을 입력해주세요")
    private String insuranceCompanyUrl;
}
