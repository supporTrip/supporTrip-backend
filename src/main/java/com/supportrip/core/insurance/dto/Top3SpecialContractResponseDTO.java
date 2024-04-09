package com.supportrip.core.insurance.dto;

import com.supportrip.core.insurance.domain.SpecialContract;
import lombok.Builder;
import lombok.Getter;

@Getter
public class Top3SpecialContractResponseDTO {

    private String name;
    private int coveragePrice;

    @Builder
    public Top3SpecialContractResponseDTO(String name, int coveragePrice) {
        this.name = name;
        this.coveragePrice = coveragePrice;
    }

    public static Top3SpecialContractResponseDTO basicDTO(SpecialContract specialContract) {
        return Top3SpecialContractResponseDTO.builder()
                .name(specialContract.getName())
                .coveragePrice(specialContract.getBasicPrice())
                .build();
    }

    public static Top3SpecialContractResponseDTO standardDTO(SpecialContract findSpecialContract) {
        return Top3SpecialContractResponseDTO.builder()
                .name(findSpecialContract.getName())
                .coveragePrice(findSpecialContract.getStandardPrice())
                .build();
    }

    public static Top3SpecialContractResponseDTO advancedDTO(SpecialContract findSpecialContract) {
        return Top3SpecialContractResponseDTO.builder()
                .name(findSpecialContract.getName())
                .coveragePrice(findSpecialContract.getAdvancedPrice())
                .build();
    }
}
