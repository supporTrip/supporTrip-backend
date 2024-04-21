package com.supportrip.core.exchange.domain;

import com.supportrip.core.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "exchange_rate")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AttributeOverride(name = "createdAt", column = @Column(name = "fetched_at"))
public class ExchangeRate extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "target_currency_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Currency targetCurrency;

    @Column(name = "target_currency_unit")
    private Long targetCurrencyUnit;

    @JoinColumn(name = "base_currency_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Currency baseCurrency;

    @Column(name = "deal_base_rate")
    private double dealBaseRate;

    @Builder(access = AccessLevel.PRIVATE)
    private ExchangeRate(Long id, Currency targetCurrency, Long targetCurrencyUnit, Currency baseCurrency, double dealBaseRate) {
        this.id = id;
        this.targetCurrency = targetCurrency;
        this.targetCurrencyUnit = targetCurrencyUnit;
        this.baseCurrency = baseCurrency;
        this.dealBaseRate = dealBaseRate;
    }

    public static ExchangeRate of(Currency targetCurrency, Long targetCurrencyUnit, Currency baseCurrency, double dealBaseRate) {
        return ExchangeRate.builder()
                .targetCurrency(targetCurrency)
                .targetCurrencyUnit(targetCurrencyUnit)
                .baseCurrency(baseCurrency)
                .dealBaseRate(dealBaseRate)
                .build();
    }
}
