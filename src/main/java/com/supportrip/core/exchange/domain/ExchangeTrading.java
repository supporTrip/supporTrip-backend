package com.supportrip.core.exchange.domain;

import com.supportrip.core.airplain.domain.AirplainCertification;
import com.supportrip.core.common.BaseEntity;
import com.supportrip.core.user.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "airplain_certification_id")
    private AirplainCertification airplainCertification;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "starting_exchange_rate_id", nullable = false)
    private ExchangeRate startingExchangeRate;

    @Column(name = "display_name", nullable = false)
    private String displayName;

    @Column(name = "trading_amount", nullable = false)
    private Long tradingAmount;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private TradingStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "strategy", nullable = false)
    private TradingStrategy strategy;

    @Column(name = "current_amount", nullable = false)
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
}
