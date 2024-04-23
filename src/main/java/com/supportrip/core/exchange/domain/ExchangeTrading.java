package com.supportrip.core.exchange.domain;

import com.supportrip.core.common.BaseEntity;
import com.supportrip.core.exchange.exception.AlreadyCompletedTradingException;
import com.supportrip.core.exchange.exception.NotEnoughTradingAmountException;
import com.supportrip.core.user.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static com.supportrip.core.exchange.domain.TradingStatus.COMPLETED;
import static com.supportrip.core.exchange.domain.TradingStatus.IN_PROGRESS;

@Entity
@Getter
@Table(name = "exchange_trading")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AttributeOverride(name = "createdAt", column = @Column(name = "began_at"))
public class ExchangeTrading extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "user_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @JoinColumn(name = "base_currency_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Currency baseCurrency;

    @JoinColumn(name = "target_currency_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Currency targetCurrency;

    @JoinColumn(name = "starting_exchange_rate_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private ExchangeRate startingExchangeRate;

    // TODO: 비행기 인증 추가

    @Column(name = "display_name")
    private String displayName;

    @Column(name = "trading_amount")
    private Long tradingAmount;

    @Column(name = "current_amount")
    private Long currentAmount;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private TradingStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "strategy")
    private TradingStrategy strategy;

    @Column(name = "target_exchange_rate")
    private Double targetExchangeRate;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @Builder(access = AccessLevel.PRIVATE)
    public ExchangeTrading(Long id, User user, Currency baseCurrency, Currency targetCurrency, ExchangeRate startingExchangeRate, String displayName, Long tradingAmount, Long currentAmount, TradingStatus status, TradingStrategy strategy, Double targetExchangeRate, LocalDateTime completedAt) {
        this.id = id;
        this.user = user;
        this.baseCurrency = baseCurrency;
        this.targetCurrency = targetCurrency;
        this.startingExchangeRate = startingExchangeRate;
        this.displayName = displayName;
        this.tradingAmount = tradingAmount;
        this.currentAmount = currentAmount;
        this.status = status;
        this.strategy = strategy;
        this.targetExchangeRate = targetExchangeRate;
        this.completedAt = completedAt;
    }

    public static ExchangeTrading of(User user, Currency baseCurrency, Currency targetCurrency, ExchangeRate startingExchangeRate, String displayName, Long tradingAmount, TradingStrategy tradingStrategy, Double targetExchangeRate, LocalDateTime completedAt) {
        return ExchangeTrading.builder()
                .user(user)
                .baseCurrency(baseCurrency)
                .targetCurrency(targetCurrency)
                .startingExchangeRate(startingExchangeRate)
                .displayName(displayName)
                .strategy(tradingStrategy)
                .tradingAmount(tradingAmount)
                .currentAmount(tradingAmount)
                .targetExchangeRate(targetExchangeRate)
                .status(IN_PROGRESS)
                .completedAt(completedAt)
                .build();
    }

    public void changeToComplete() {
        if (COMPLETED.equals(this.status)) {
            throw new AlreadyCompletedTradingException();
        }
        this.status = COMPLETED;
    }

    public void reduceAmount(Long amount) {
        if (this.currentAmount < amount) {
            throw new NotEnoughTradingAmountException();
        }
        this.currentAmount -= amount;
    }

    public boolean isLastDate(LocalDate date) {
        // TODO: 당일 몇시에 완료되도록 할 건지 정해야 함, 즉 마지막 거래시점이 언제인지 설정
        return completedAt.toLocalDate().isEqual(date);
    }

//    public Long getMaxExchangableAmount() {
//
//    }
}
