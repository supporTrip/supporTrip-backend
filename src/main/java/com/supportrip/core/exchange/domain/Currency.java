package com.supportrip.core.exchange.domain;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "country_id")
    private Country country;

    @Column(name = "name")
    private String name;

    @Column(name = "code")
    private String code;

    @Column(name = "sign")
    private String sign;

    @Builder(access = AccessLevel.PRIVATE)
    private Currency(Long id, Country country, String name, String code, String sign) {
        this.id = id;
        this.country = country;
        this.name = name;
        this.code = code;
        this.sign = sign;
    }

    public static Currency of(Country country, String name, String code, String sign) {
        return Currency.builder()
                .country(country)
                .name(name)
                .code(code)
                .sign(sign)
                .build();
    }
}
