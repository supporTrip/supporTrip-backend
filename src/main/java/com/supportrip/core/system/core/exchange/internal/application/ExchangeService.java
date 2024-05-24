package com.supportrip.core.system.core.exchange.internal.application;

import com.supportrip.core.context.error.exception.forbidden.ExchangeAccessDeniedException;
import com.supportrip.core.context.error.exception.notfound.*;
import com.supportrip.core.system.core.account.internal.application.ForeignAccountService;
import com.supportrip.core.system.core.account.internal.application.LinkedAccountService;
import com.supportrip.core.system.core.account.internal.application.PointWalletService;
import com.supportrip.core.system.core.account.internal.domain.*;
import com.supportrip.core.system.core.airplane.internal.application.AirplaneService;
import com.supportrip.core.system.core.airplane.internal.domain.AirplaneCertification;
import com.supportrip.core.system.core.exchange.internal.domain.*;
import com.supportrip.core.system.core.exchange.internal.presentation.request.CreateExchangeTradingRequest;
import com.supportrip.core.system.core.exchange.internal.presentation.response.ExchangeTradingResponse;
import com.supportrip.core.system.core.user.internal.domain.User;
import com.supportrip.core.system.core.user.internal.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExchangeService {
    private final ExchangeRateService exchangeRateService;
    private final ForeignAccountService foreignAccountService;
    private final ForeignAccountTransactionRepository foreignAccountTransactionRepository;
    private final ExchangeTradingRepository exchangeTradingRepository;
    private final UserRepository userRepository;
    private final CountryRepository countryRepository;
    private final CurrencyRepository currencyRepository;
    private final ForeignAccountRepository foreignAccountRepository;
    private final AirplaneService airplaneService;
    private final ExchangeRateRepository exchangeRateRepository;
    private final LinkedAccountService linkedAccountService;
    private final PointWalletService pointWalletService;
    private final ForeignCurrencyWalletRepository foreignCurrencyWalletRepository;

    @Transactional
    public void exchange(ExchangeTrading exchangeTrading, Long amountDividedByCurrencyUnit) {
        if (amountDividedByCurrencyUnit <= 0) {
            return;
        }

        Currency targetCurrency = exchangeTrading.getTargetCurrency();
        User user = exchangeTrading.getUser();

        ExchangeRate latestExchangeRate = exchangeRateService.getLatestExchangeRate(targetCurrency);
        long fromAmount = calculateAmountForExchange(amountDividedByCurrencyUnit, latestExchangeRate);

        exchangeTrading.reduceAmount(fromAmount);

        final long currencyAmount = amountDividedByCurrencyUnit * latestExchangeRate.getTargetCurrencyUnit();
        ForeignCurrencyWallet foreignCurrencyWallet = foreignAccountService.getForeignCurrencyWallet(user, targetCurrency);
        foreignCurrencyWallet.deposit(currencyAmount);

        ForeignAccountTransaction foreignAccountTransaction = ForeignAccountTransaction.of(
                currencyAmount,
                latestExchangeRate.getDealBaseRate(),
                foreignCurrencyWallet.getTotalAmount(),
                foreignCurrencyWallet,
                exchangeTrading
        );

        foreignAccountTransactionRepository.save(foreignAccountTransaction);
    }

    private static long calculateAmountForExchange(Long toAmount, ExchangeRate latestExchangeRate) {
        return (long) (toAmount * latestExchangeRate.getDealBaseRate());
    }

    public List<ExchangeTradingResponse> getInProgressExchangeTradings(User user) {
        if (foreignAccountRepository.findByUser(user).isEmpty()) {
            throw new ExchangeAccessDeniedException();
        }

        return exchangeTradingRepository.findByUserAndStatus(user, TradingStatus.IN_PROGRESS).stream()
                .map((et) -> {
                    Country baseCountry = countryRepository.findByCurrency(et.getBaseCurrency());
                    Country targetCountry = countryRepository.findByCurrency(et.getTargetCurrency());

                    return ExchangeTradingResponse.of(et, baseCountry, targetCountry);
                })
                .collect(Collectors.toList());
    }

    public List<Currency> getExchangeableCurrency() {
        return currencyRepository.findByCodeNot("KRW");
    }

    @Transactional
    public Long createExchangeTrading(User user, CreateExchangeTradingRequest request) {
        if (foreignAccountRepository.findByUser(user).isEmpty()) {
            throw new ExchangeAccessDeniedException();
        }

        pointWalletService.withdrawPoint(user, request.getPoint());

        Country country = countryRepository.findById(request.getCountryId()).orElseThrow(CountryNotFoundException::new);
        AirplaneCertification airplaneCertification = airplaneService.createAirplaneCertification(country, request.getPnrNumber(), request.getDepartAt());

        Currency baseCurrency = currencyRepository.findByCode("KRW").orElseThrow(CurrencyNotFoundException::new);
        Currency targetCurrency = currencyRepository.findById(request.getTargetCurrencyId()).orElseThrow(CurrencyNotFoundException::new);
        ExchangeRate startingExchangeRate = exchangeRateRepository.findById(request.getStartingExchangeRateId()).orElseThrow(ExchangeRateNotFoundException::new);

        ExchangeTrading newExchangeTrading = exchangeTradingRepository.save(
                ExchangeTrading.of(user,
                        baseCurrency,
                        targetCurrency,
                        startingExchangeRate,
                        airplaneCertification,
                        request.getDisplayName(),
                        request.getTradingAmount(),
                        TradingStrategy.valueOf(request.getStrategy()),
                        request.getTargetExchangeRate(),
                        request.getCompleteDate()
                )
        );

        linkedAccountService.withdrawMoney(user, request.getTradingAmount());

        ForeignAccount foreignAccount = foreignAccountRepository.findByUser(user).orElseThrow(ForeignAccountNotFoundException::new);
        Currency krw = currencyRepository.findByCode("KRW").orElseThrow(CurrencyNotFoundException::new);
        ForeignCurrencyWallet foreignCurrencyWallet = foreignCurrencyWalletRepository.findByForeignAccountAndCurrency(foreignAccount, krw).orElseThrow();
        foreignCurrencyWallet.deposit(request.getTradingAmount());

        ForeignAccountTransaction foreignAccountTransaction = ForeignAccountTransaction.of(
                request.getTradingAmount(),
                null,
                foreignCurrencyWallet.getTotalAmount(),
                foreignCurrencyWallet,
                newExchangeTrading
        );

        foreignAccountTransactionRepository.save(foreignAccountTransaction);

        return newExchangeTrading.getId();
    }
}
