package com.supportrip.core.account.domain;

import com.supportrip.core.exchange.domain.ExchangeTrading;
import com.supportrip.core.user.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "foreign_account_transaction")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ForeignAccountTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "amount")
    private Double amount;

    @Column(name = "transacted_at")
    private LocalDateTime transactedAt;

    @Column(name = "target_exchange_rate")
    private Double targetExchangeRate;

    @Column(name = "target_currency_total_amount")
    private Double targetCurrencyTotalAmount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "foreign_account_id")
    private ForeignAccount foreignAccount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exchange_trading_id")
    private ExchangeTrading exchangeTrading;

    @Builder(access = AccessLevel.PRIVATE)
    private ForeignAccountTransaction(Long id, Double amount, LocalDateTime transactedAt, Double targetExchangeRate, Double targetCurrencyTotalAmount, ForeignAccount foreignAccount, ExchangeTrading exchangeTrading) {
        this.id = id;
        this.amount = amount;
        this.transactedAt = transactedAt;
        this.targetExchangeRate = targetExchangeRate;
        this.targetCurrencyTotalAmount = targetCurrencyTotalAmount;
        this.foreignAccount = foreignAccount;
        this.exchangeTrading = exchangeTrading;
    }

    public static ForeignAccountTransaction of(Double amount, LocalDateTime transactedAt, Double targetExchangeRate, Double targetCurrencyTotalAmount,
                                               ForeignAccount foreignAccount, ExchangeTrading exchangeTrading){
        return ForeignAccountTransaction.builder()
                .amount(amount)
                .transactedAt(transactedAt)
                .targetExchangeRate(targetExchangeRate)
                .targetCurrencyTotalAmount(targetCurrencyTotalAmount)
                .foreignAccount(foreignAccount)
                .exchangeTrading(exchangeTrading)
                .build();
    }
}
