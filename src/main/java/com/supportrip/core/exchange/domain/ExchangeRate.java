package com.supportrip.core.exchange.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "exchange_rate")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AttributeOverride(name = "createdAt", column = @Column(name = "fetched_at"))
public class ExchangeRate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "target_currency_id")
    private Currency targetCurrency;
    @ManyToOne
    @JoinColumn(name = "base_currency_id")
    private Currency baseCurrency;
    @Column(name = "deal_base_rate")
    private Float dealBaseRate;

//    @Column(name = "target_currency_unit")
//    private Long targetCurrencyUnit;
}
