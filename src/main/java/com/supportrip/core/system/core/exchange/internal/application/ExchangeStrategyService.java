package com.supportrip.core.system.core.exchange.internal.application;

import com.supportrip.core.system.core.exchange.internal.domain.ExchangeTrading;

import java.time.LocalDate;

public interface ExchangeStrategyService {
    void execute(ExchangeTrading exchangeTrading, LocalDate today);

    boolean canApply(ExchangeTrading exchangeTrading);
}
