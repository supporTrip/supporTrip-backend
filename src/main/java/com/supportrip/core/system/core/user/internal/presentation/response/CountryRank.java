package com.supportrip.core.system.core.user.internal.presentation.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CountryRank {
    private int rank;
    private String countryName;
    private Long count;
    private Long amount;

    @Builder(access = AccessLevel.PRIVATE)
    public CountryRank(int rank, String countryName, Long count, Long amount) {
        this.rank = rank;
        this.countryName = countryName;
        this.count = count;
        this.amount = amount;
    }

    public static CountryRank of(int rank, String countryName, Long count, Long amount) {
        return CountryRank.builder()
                .rank(rank)
                .countryName(countryName)
                .count(count)
                .amount(amount)
                .build();
    }

    public static CountryRank from(CountryRankingResult countryRankingResult) {
        return of(
                countryRankingResult.getRank(),
                countryRankingResult.getCountry().getName(),
                countryRankingResult.getCount(),
                countryRankingResult.getAmount()
        );
    }
}
