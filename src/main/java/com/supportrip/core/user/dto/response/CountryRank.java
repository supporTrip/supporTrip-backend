package com.supportrip.core.user.dto.response;

import lombok.*;

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
}
