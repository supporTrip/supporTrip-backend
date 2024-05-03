package com.supportrip.core.system.batch;

import com.supportrip.core.system.common.external.SmsService;
import com.supportrip.core.system.core.exchange.internal.domain.Country;
import com.supportrip.core.system.core.exchange.internal.domain.Currency;
import com.supportrip.core.system.core.exchange.internal.domain.ExchangeRate;
import com.supportrip.core.system.core.exchange.internal.domain.ExchangeRateRangeStatistics;
import com.supportrip.core.system.core.exchange.internal.application.ExchangeRateService;
import com.supportrip.core.system.core.exchange.internal.application.ExchangeRateStatisticsService;
import com.supportrip.core.system.core.exchange.internal.application.ExchangeService;
import com.supportrip.core.system.core.user.internal.domain.User;
import com.supportrip.core.system.core.user.internal.domain.UserNotificationStatus;
import com.supportrip.core.system.core.user.internal.presentation.response.CountryRankingResult;
import com.supportrip.core.system.core.user.internal.domain.UserNotificationStatusRepository;
import com.supportrip.core.system.core.user.internal.application.UserCardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.supportrip.core.system.core.exchange.internal.domain.PeriodUnit.THREE_MONTH;

@Slf4j
@Component
@RequiredArgsConstructor
public class RemindScheduler {
    private final UserNotificationStatusRepository userNotificationStatusRepository;
    private final ExchangeRateStatisticsService exchangeRateStatisticsService;
    private final ExchangeService exchangeService;
    private final ExchangeRateService exchangeRateService;
    private final SmsService smsService;
    private final UserCardService userCardService;

    // 3개월마다 1번 동작
    @Scheduled(cron = "0 0 0 1 */3 *")
    public void remindLowCostCurrencyToUser() {
        List<Currency> exchangeableCurrencies = exchangeService.getExchangeableCurrency();
        List<Currency> lowCostCurrencies = filterLowCostCurrencies(exchangeableCurrencies);

        List<UserNotificationStatus> userNotificationStatus = userNotificationStatusRepository.findAll();
        List<User> notifiableUser = filterNotifiableUser(userNotificationStatus);

        LocalDate today = LocalDate.now();
        LocalDate beforeThreeMonth = today.minusMonths(3);
        Set<Long> lowCostCurrencyIds = lowCostCurrencies.stream()
                .map(Currency::getId)
                .collect(Collectors.toSet());

        notifiableUser.forEach(user -> {
                    List<CountryRankingResult> countryRankingResults =
                            userCardService.getCountryRankingResult(user, beforeThreeMonth, today);

                    List<Currency> lowCostRankedCurrencies =
                            getLowCostTop3RankedCurrencies(countryRankingResults, lowCostCurrencyIds);

                    if (lowCostRankedCurrencies.isEmpty()) {
                        return;
                    }

                    smsService.sendOne(makeMessage(user.getName(), lowCostRankedCurrencies), user.getSmsPhoneNumber());
                }
        );
    }

    private String makeMessage(String name, List<Currency> lowCostCurrencies) {
        String currencyNames = lowCostCurrencies.stream()
                .map(Currency::getName)
                .collect(Collectors.joining(", "));

        return String.format("[서포트립] %s님이 이전에 자주 결제하신 %s(이)가 현재 3개월 평균 환율보다 저렴해요. " +
                "서포트립을 통해 자동으로 환전되는 편리한 경험을 즐겨보세요!", name, currencyNames);
    }

    @NotNull
    private static List<Currency> getLowCostTop3RankedCurrencies(List<CountryRankingResult> countryRankingResults,
                                                                 Set<Long> lowCostCurrencyIds) {
        return countryRankingResults.stream()
                .limit(3)
                .map(CountryRankingResult::getCountry)
                .map(Country::getCurrency)
                .filter(currency -> lowCostCurrencyIds.contains(currency.getId()))
                .toList();
    }

    @NotNull
    private List<User> filterNotifiableUser(List<UserNotificationStatus> userNotificationStatus) {
        return userNotificationStatus.stream()
                .filter(UserNotificationStatus::getStatus)
                .map(UserNotificationStatus::getUser)
                .toList();
    }

    @NotNull
    private List<Currency> filterLowCostCurrencies(List<Currency> exchangeableCurrencies) {
        return exchangeableCurrencies.stream()
                .filter(currency -> {
                    ExchangeRateRangeStatistics exchangeRateAverage =
                            exchangeRateStatisticsService.getExchangeRateAverage(currency, THREE_MONTH);

                    ExchangeRate latestExchangeRate = exchangeRateService.getLatestExchangeRate(currency);

                    return exchangeRateAverage.getExchangeRate() > latestExchangeRate.getDealBaseRate();
                })
                .toList();
    }
}
