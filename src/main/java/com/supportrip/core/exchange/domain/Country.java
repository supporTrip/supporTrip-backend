package com.supportrip.core.exchange.domain;

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

    @Column(name = "country_name")
    private String name;

    @Column(name = "country_flag_url")
    private String flagUrl;

    @Column(name = "country_curreny_name")
    private String currency_name;

    @Builder(access = AccessLevel.PRIVATE)
    private Country(Long id, String name, String flagUrl, String currency_name) {
        this.id = id;
        this.name = name;
        this.flagUrl = flagUrl;
        this.currency_name = currency_name;
    }

    public static Country of(String name, String flagUrl, String currency_name){
        return Country.builder()
                .name(name)
                .flagUrl(flagUrl)
                .currency_name(currency_name)
                .build();
    }
}
