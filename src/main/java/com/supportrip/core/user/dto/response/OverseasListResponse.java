package com.supportrip.core.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OverseasListResponse {
    private List<CountryRank> ranking;
    private List<OverSeasHistory> overSeasHistories;

    public static OverseasListResponse of(List<CountryRank> ranking, List<OverSeasHistory> overSeasHistories) {
        return OverseasListResponse.builder()
                .ranking(ranking)
                .overSeasHistories(overSeasHistories)
                .build();
    }
}
