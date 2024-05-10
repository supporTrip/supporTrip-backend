package com.supportrip.core.context.config;

import com.supportrip.core.system.core.exchange.internal.application.ExchangeStrategyManager;
import com.supportrip.core.system.core.exchange.internal.application.ExchangeStrategyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class ExchangeStrategyConfig {
    @Bean
    public ExchangeStrategyManager exchangeStrategyManager(List<ExchangeStrategyService> exchangeStrategyServices) {
        log.info("{} ExchangeStrategyService registered: {}",
                exchangeStrategyServices.size(),
                concatExchangeStrategyServiceNames(exchangeStrategyServices)
        );

        return new ExchangeStrategyManager(exchangeStrategyServices);
    }

    private String concatExchangeStrategyServiceNames(List<ExchangeStrategyService> exchangeStrategyServices) {
        return exchangeStrategyServices.stream()
                .map(ExchangeStrategyService::getClass)
                .map(Class::getSimpleName)
                .collect(Collectors.joining(","));
    }
}
