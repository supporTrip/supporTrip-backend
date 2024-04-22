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

    @Column(name = "display_name")
    private String displayName;

    @Column(name = "trading_amount")
    private Long tradingAmount;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private TradingStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "strategy")
    private TradingStrategy strategy;

    @Column(name = "current_amount")
    private Long currentAmount;

    @Column(name = "target_price")
    private Double targetPrice;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @Builder(access = AccessLevel.PRIVATE)
    public ExchangeTrading(Long id, User user, String displayName, Long tradingAmount, TradingStatus status, TradingStrategy strategy, Long currentAmount, Double targetPrice, LocalDateTime completedAt) {
        this.id = id;
        this.user = user;
        this.displayName = displayName;
        this.tradingAmount = tradingAmount;
        this.status = status;
        this.strategy = strategy;
        this.currentAmount = currentAmount;
        this.targetPrice = targetPrice;
        this.completedAt = completedAt;
    }

    public static ExchangeTrading of(User user, String displayName, Long tradingAmount, TradingStrategy tradingStrategy, Double targetPrice, LocalDateTime completedAt) {
        return ExchangeTrading.builder()
                .user(user)
                .displayName(displayName)
                .strategy(tradingStrategy)
                .tradingAmount(tradingAmount)
                .currentAmount(tradingAmount)
                .targetPrice(targetPrice)
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
}
