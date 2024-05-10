package com.supportrip.core.system.core.exchange.internal.application;

import com.supportrip.core.system.core.exchange.internal.domain.ExchangeTrading;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
public class ExchangeStrategyManager {
    private final List<ExchangeStrategyService> exchangeStrategyServices;

    public void executeExchangeStrategy(ExchangeTrading exchangeTrading, LocalDate today) {
        for (ExchangeStrategyService exchangeStrategyService : exchangeStrategyServices) {
            if (exchangeStrategyService.canApply(exchangeTrading)) {
                exchangeStrategyService.execute(exchangeTrading, today);
                return;
            }
        }

        throw new IllegalStateException("등록된 환전 알고리즘과 일치하는 환전 전략이 없어 환전을 진행할 수 없습니다.");
    }
}