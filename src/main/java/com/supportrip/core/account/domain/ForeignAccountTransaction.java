package com.supportrip.core.account.domain;

import com.supportrip.core.common.BaseEntity;
import com.supportrip.core.exchange.domain.ExchangeTrading;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "foreign_account_transaction")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AttributeOverride(name = "createdAt", column = @Column(name = "transacted_at"))
public class ForeignAccountTransaction extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "amount")
    private Long amount;

    @Column(name = "target_exchange_rate")
    private Double targetExchangeRate;

    @Column(name = "target_currency_total_amount")
    private Long targetCurrencyTotalAmount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "foreign_currency_wallet_id")
    private ForeignCurrencyWallet foreignCurrencyWallet;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exchange_trading_id")
    private ExchangeTrading exchangeTrading;

    @Builder(access = AccessLevel.PRIVATE)
    private ForeignAccountTransaction(Long id, Long amount, Double targetExchangeRate, Long targetCurrencyTotalAmount, ForeignCurrencyWallet foreignCurrencyWallet, ExchangeTrading exchangeTrading) {
        this.id = id;
        this.amount = amount;
        this.targetExchangeRate = targetExchangeRate;
        this.targetCurrencyTotalAmount = targetCurrencyTotalAmount;
        this.foreignCurrencyWallet = foreignCurrencyWallet;
        this.exchangeTrading = exchangeTrading;
    }

    public static ForeignAccountTransaction of(Long amount,
                                               Double targetExchangeRate,
                                               Long targetCurrencyTotalAmount,
                                               ForeignCurrencyWallet foreignCurrencyWallet,
                                               ExchangeTrading exchangeTrading) {
        return ForeignAccountTransaction.builder()
                .amount(amount)
                .targetExchangeRate(targetExchangeRate)
                .targetCurrencyTotalAmount(targetCurrencyTotalAmount)
                .foreignCurrencyWallet(foreignCurrencyWallet)
                .exchangeTrading(exchangeTrading)
                .build();
    }
}
