package com.supportrip.core.insurance.dto;

import com.supportrip.core.insurance.domain.SpecialContract;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
public class SpecialContractResponse {
    private Long id;
    private String name;
    private String description;
    private int standardPrice;
    private int advancedPrice;

    @Builder(access = AccessLevel.PRIVATE)
    private SpecialContractResponse(Long id, String name, String description, int standardPrice, int advancedPrice) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.standardPrice = standardPrice;
        this.advancedPrice = advancedPrice;
    }

    public static SpecialContractResponse toDTO(SpecialContract specialContract) {
        return SpecialContractResponse.builder()
                .id(specialContract.getId())
                .name(specialContract.getName())
                .description(specialContract.getDescription())
                .standardPrice(specialContract.getStandardPrice())
                .advancedPrice(specialContract.getAdvancedPrice())
                .build();
    }
}
