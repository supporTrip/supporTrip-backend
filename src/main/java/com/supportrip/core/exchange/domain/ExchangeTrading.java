package com.supportrip.core.exchange.domain;

import com.supportrip.core.airplain.domain.AirplainCertification;
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
import java.time.temporal.ChronoUnit;

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

    @JoinColumn(name = "user_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "base_currency_id", nullable = false)
    private Currency baseCurrency;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_currency_id", nullable = false)
    private Currency targetCurrency;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "starting_exchange_rate_id", nullable = false)
    private ExchangeRate startingExchangeRate;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "airplain_certification_id")
    private AirplainCertification airplainCertification;

    @Column(name = "display_name", nullable = false)
    private String displayName;

    @Column(name = "trading_amount", nullable = false)
    private Long tradingAmount;

    @Column(name = "current_amount")
    private Long currentAmount;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private TradingStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "strategy", nullable = false)
    private TradingStrategy strategy;

    @Column(name = "target_exchange_rate")
    private Double targetExchangeRate;

    @Column(name = "complete_date")
    private LocalDate completeDate;

    @Builder(access = AccessLevel.PRIVATE)
    public ExchangeTrading(Long id, User user, Currency baseCurrency, Currency targetCurrency, ExchangeRate startingExchangeRate, AirplainCertification airplainCertification, String displayName, Long tradingAmount, Long currentAmount, TradingStatus status, TradingStrategy strategy, Double targetExchangeRate, LocalDate completeDate) {
        this.id = id;
        this.user = user;
        this.baseCurrency = baseCurrency;
        this.targetCurrency = targetCurrency;
        this.startingExchangeRate = startingExchangeRate;
        this.airplainCertification = airplainCertification;
        this.displayName = displayName;
        this.tradingAmount = tradingAmount;
        this.currentAmount = currentAmount;
        this.status = status;
        this.strategy = strategy;
        this.targetExchangeRate = targetExchangeRate;
        this.completeDate = completeDate;
    }

    public static ExchangeTrading of(User user, Currency baseCurrency, Currency targetCurrency, ExchangeRate startingExchangeRate, AirplainCertification airplainCertification, String displayName, Long tradingAmount, TradingStrategy tradingStrategy, Double targetExchangeRate, LocalDate completeDate) {
        return ExchangeTrading.builder()
                .user(user)
                .baseCurrency(baseCurrency)
                .targetCurrency(targetCurrency)
                .startingExchangeRate(startingExchangeRate)
                .airplainCertification(airplainCertification)
                .displayName(displayName)
                .strategy(tradingStrategy)
                .tradingAmount(tradingAmount)
                .currentAmount(tradingAmount)
                .targetExchangeRate(targetExchangeRate)
                .status(IN_PROGRESS)
                .completeDate(completeDate)
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
        return this.completeDate.isEqual(date);
    }

    public long getExchangeAmount(int remainDays) {
        if (remainDays < 1) {
            throw new IllegalArgumentException("잘못된 환전 거래 남은 일수 입니다.");
        }
        return this.currentAmount / remainDays;
    }

    public long getMaxExchangeableAmount(double dealBaseRate) {
        if (dealBaseRate <= 0.0) {
            throw new IllegalArgumentException("잘못된 환율 정보입니다.");
        }
        long quotient = (long) (this.currentAmount / dealBaseRate);
        return (long) (quotient * dealBaseRate);
    }

    public long flushCurrentAmount() {
        long amount = this.currentAmount;
        this.currentAmount = 0L;
        return amount;
    }

    public int getRemainDays(LocalDate today) {
        return (int) ChronoUnit.DAYS.between(today, this.completeDate) + 1;
    }
}
