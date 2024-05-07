package com.supportrip.core.system.core.exchange.internal.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "country")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Country {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "country_flag_url")
    private String flagUrl;

    @Column(name = "country_currency_name")
    private String currency_name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "currency_id")
    private Currency currency;

    @Column(name = "code")
    private String code;

    @Builder(access = AccessLevel.PRIVATE)
    private Country(Long id, String name, String flagUrl, String currency_name, Currency currency, String code) {
        this.id = id;
        this.name = name;
        this.flagUrl = flagUrl;
        this.currency_name = currency_name;
        this.currency = currency;
        this.code = code;
    }

    public static Country of(String name, String flagUrl, String currency_name, Currency currency, String code){
        return Country.builder()
                .name(name)
                .flagUrl(flagUrl)
                .currency_name(currency_name)
                .currency(currency)
                .code(code)
                .build();
    }
}
