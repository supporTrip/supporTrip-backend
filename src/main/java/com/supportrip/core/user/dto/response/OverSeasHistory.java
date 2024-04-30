package com.supportrip.core.user.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class OverSeasHistory {
    private String countryName;
    private LocalDate departDate;

    private OverSeasHistory(String countryName, LocalDate departDate) {
        this.countryName = countryName;
        this.departDate = departDate;
    }

    public static OverSeasHistory of(String countryName, LocalDate departDate) {
        return new OverSeasHistory(countryName, departDate);
    }
}
