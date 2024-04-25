package com.supportrip.core.insurance.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class AdminSpecialContractsRequest {
    private Long id;

    @NotNull(message = "특약 이름을 입력해주세요.")
    private String name;

    @NotNull(message = "특약 상세내용을 입력해주세요.")
    private String description;

    @NotNull(message = "플랜 표준 가격을 입력해주세요.")
    private int standardPrice;

    @NotNull(message = "플랜 고급 가격을 입력해주세요.")
    private int advancedPrice;

    private Long flightInsuranceId;
}
