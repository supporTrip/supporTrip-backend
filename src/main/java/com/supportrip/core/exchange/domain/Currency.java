package com.supportrip.core.exchange.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "currency")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Currency {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "country_id")
    private Country country;

    @Column(name = "currency_name")
    private String name;

    @Column(name = "currency_unit")
    private String unit;

    @Column(name = "currency_sign")
    private String sign;

    @Builder(access = AccessLevel.PRIVATE)
    private Currency(Long id, Country country, String name, String unit, String sign) {
        this.id = id;
        this.country = country;
        this.name = name;
        this.unit = unit;
        this.sign = sign;
    }

    public static Currency of(Country country, String name, String unit, String sign){
        return Currency.builder()
                .country(country)
                .name(name)
                .unit(unit)
                .sign(sign)
                .build();
    }
}
