package com.supportrip.core.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CountryRank {
    private int rank;
    private String countryName;
    private Long count;
    private Long amount;
}
