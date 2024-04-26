package com.supportrip.core.exchange.service;

import com.supportrip.core.exchange.domain.ExchangeTrading;

import java.time.LocalDate;

public interface ExchangeStrategyService {
    void execute(ExchangeTrading exchangeTrading, LocalDate today);

    boolean canApply(ExchangeTrading exchangeTrading);
}
