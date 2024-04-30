package com.supportrip.core.config;

import com.supportrip.core.account.service.ForeignAccountService;
import com.supportrip.core.account.service.PointWalletService;
import com.supportrip.core.common.SmsService;
import com.supportrip.core.exchange.service.*;
import com.supportrip.core.user.repository.UserNotificationStatusRepository;
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
    private final ExchangeService exchangeService;
    private final ExchangeRateStatisticsService exchangeRateStatisticsService;
    private final ExchangeRateService exchangeRateService;
    private final PointWalletService pointWalletService;
    private final ForeignAccountService foreignAccountService;
    private final SmsService smsService;
    private final UserNotificationStatusRepository userNotificationStatusRepository;

    @Bean
    public ExchangeStrategyManager exchangeStrategyManager() {
        List<ExchangeStrategyService> exchangeStrategyServices = List.of(
                new StableExchangeStrategyService(
                        exchangeService,
                        exchangeRateStatisticsService,
                        exchangeRateService,
                        pointWalletService,
                        foreignAccountService,
                        smsService,
                        userNotificationStatusRepository
                ),
                new TargetExchangeStrategyService(
                        exchangeService,
                        pointWalletService,
                        exchangeRateService,
                        smsService,
                        userNotificationStatusRepository
                )
        );

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
