package com.supportrip.core.system.core.user.internal.presentation.response;

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
    private String status;

    private OverSeasHistory(String countryName, LocalDateTime approvedAt, Double amount, String currencyCode, String status) {
        this.countryName = countryName;
        this.approvedAt = approvedAt;
        this.amount = amount;
        this.currencyCode = currencyCode;
        this.status = status;
    }

    public static OverSeasHistory of(String countryName, LocalDateTime approvedAt, Double amount, String currencyCode, String status) {
        return new OverSeasHistory(countryName, approvedAt, amount, currencyCode, status);
    }
}
