package com.supportrip.core.insurance.dto;

import com.supportrip.core.insurance.domain.SpecialContract;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
public class Top3SpecialContractResponse {

    private String name;
    private int coveragePrice;

    @Builder(access = AccessLevel.PRIVATE)
    private Top3SpecialContractResponse(String name, int coveragePrice) {
        this.name = name;
        this.coveragePrice = coveragePrice;
    }

    public static Top3SpecialContractResponse standard(SpecialContract findSpecialContract) {
        return Top3SpecialContractResponse.builder()
                .name(findSpecialContract.getName())
                .coveragePrice(findSpecialContract.getStandardPrice())
                .build();
    }

    public static Top3SpecialContractResponse advanced(SpecialContract findSpecialContract) {
        return Top3SpecialContractResponse.builder()
                .name(findSpecialContract.getName())
                .coveragePrice(findSpecialContract.getAdvancedPrice())
                .build();
    }
}
