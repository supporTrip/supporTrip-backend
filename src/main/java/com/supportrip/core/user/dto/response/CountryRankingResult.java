package com.supportrip.core.user.dto.response;

import com.supportrip.core.exchange.domain.Country;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
public class CountryRankingResult {
    private final int rank;
    private final Country country;
    private final long count;
    private final long amount;

    @Builder(access = AccessLevel.PRIVATE)
    private CountryRankingResult(int rank, Country country, long count, long amount) {
        this.rank = rank;
        this.country = country;
        this.count = count;
        this.amount = amount;
    }

    public static CountryRankingResult of(int rank, Country country, long count, long amount) {
        return CountryRankingResult.builder()
                .rank(rank)
                .country(country)
                .count(count)
                .amount(amount)
                .build();
    }
}
