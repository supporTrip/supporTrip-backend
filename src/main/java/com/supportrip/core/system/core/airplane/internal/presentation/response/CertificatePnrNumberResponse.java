package com.supportrip.core.system.core.airplane.internal.presentation.response;

import com.supportrip.core.system.core.exchange.internal.domain.Country;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CertificatePnrNumberResponse {
    private final Long countryId;
    private final String country;
    private final String countryCurrency;
    private final LocalDateTime departAt;

    @Builder
    private CertificatePnrNumberResponse(Long countryId, String country, String countryCurrency, LocalDateTime departAt) {
        this.countryId = countryId;
        this.country = country;
        this.countryCurrency = countryCurrency;
        this.departAt = departAt;
    }

    public static CertificatePnrNumberResponse of(Country country, LocalDateTime departAt) {
        return CertificatePnrNumberResponse.builder()
                .countryId(country.getId())
                .country(country.getName())
                .countryCurrency(country.getCurrency_name())
                .departAt(departAt)
                .build();
    }
}
