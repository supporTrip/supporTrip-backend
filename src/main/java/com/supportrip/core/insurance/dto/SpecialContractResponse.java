package com.supportrip.core.insurance.dto;

import com.supportrip.core.insurance.domain.SpecialContract;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
public class SpecialContractResponse {
    private String name;
    private String description;
    private int standardPrice;
    private int advancedPrice;

    @Builder(access = AccessLevel.PRIVATE)
    private SpecialContractResponse(String name, String description, int standardPrice, int advancedPrice) {
        this.name = name;
        this.description = description;
        this.standardPrice = standardPrice;
        this.advancedPrice = advancedPrice;
    }

    public static SpecialContractResponse toDTO(SpecialContract specialContract) {
        return SpecialContractResponse.builder()
                .name(specialContract.getName())
                .description(specialContract.getDescription())
                .standardPrice(specialContract.getStandardPrice())
                .advancedPrice(specialContract.getAdvancedPrice())
                .build();
    }
}
