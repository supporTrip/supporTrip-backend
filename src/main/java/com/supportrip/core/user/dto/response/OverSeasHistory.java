package com.supportrip.core.user.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class OverSeasHistory {
    private String countryName;
    private LocalDateTime approvedAt;
    private Double amount;
    private String currencyCode;

    private OverSeasHistory(String countryName, LocalDateTime approvedAt, Double amount, String currencyCode) {
        this.countryName = countryName;
        this.approvedAt = approvedAt;
        this.amount = amount;
        this.currencyCode = currencyCode;
    }

    public static OverSeasHistory of(String countryName, LocalDateTime approvedAt, Double amount, String currencyCode) {
        return new OverSeasHistory(countryName, approvedAt, amount, currencyCode);
    }
}
