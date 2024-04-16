package com.supportrip.core.exchange.domain;

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

    @Column(name = "display_name")
    private String displayName;

    @Column(name = "trading_amount")
    private String tradingAmount;

    @Column(name = "status")
    private String status;

    @Column(name = "strategy")
    private String strategy;

    @Column(name = "current_amount")
    private String currentAmount;

    @Column(name = "target_price")
    private String targetPrice;

    @Column(name = "began_at")
    private LocalDateTime beganAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

}
