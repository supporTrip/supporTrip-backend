package com.supportrip.core.exchange.domain;

import lombok.Getter;

@Getter
public enum TradingStrategy {
    STABLE("안정형"), TARGET("목표형");

    private String name;
    TradingStrategy(String name) {
        this.name = name;
    }
}
