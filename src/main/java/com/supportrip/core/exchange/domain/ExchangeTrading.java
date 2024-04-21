package com.supportrip.core.exchange.domain;

import com.supportrip.core.airplain.domain.AirplainCertification;
import com.supportrip.core.user.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "exchange_trading")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ExchangeTrading {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "base_currency_id", nullable = false)
    private Currency baseCurrency;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_currency_id")
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
    private String tradingAmount;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "strategy", nullable = false)
    private String strategy;

    @Column(name = "current_amount", nullable = false)
    private Long currentAmount;

    @Column(name = "target_price")
    private Float targetPrice;

    @Column(name = "began_at", nullable = false)
    private LocalDateTime beganAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

}
