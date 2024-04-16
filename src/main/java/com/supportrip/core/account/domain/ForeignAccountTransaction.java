package com.supportrip.core.account.domain;

import com.supportrip.core.common.BaseEntity;
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
@AttributeOverride(name = "createdAt", column = @Column(name = "transacted_at"))
public class ForeignAccountTransaction extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "amount")
    private Double amount;

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
    private ForeignAccountTransaction(Long id, Double amount, Double targetExchangeRate, Double targetCurrencyTotalAmount, ForeignAccount foreignAccount, ExchangeTrading exchangeTrading) {
        this.id = id;
        this.amount = amount;
        this.targetExchangeRate = targetExchangeRate;
        this.targetCurrencyTotalAmount = targetCurrencyTotalAmount;
        this.foreignAccount = foreignAccount;
        this.exchangeTrading = exchangeTrading;
    }

    public static ForeignAccountTransaction of(Double amount, Double targetExchangeRate, Double targetCurrencyTotalAmount,
                                               ForeignAccount foreignAccount, ExchangeTrading exchangeTrading){
        return ForeignAccountTransaction.builder()
                .amount(amount)
                .targetExchangeRate(targetExchangeRate)
                .targetCurrencyTotalAmount(targetCurrencyTotalAmount)
                .foreignAccount(foreignAccount)
                .exchangeTrading(exchangeTrading)
                .build();
    }
}
