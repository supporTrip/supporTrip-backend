package com.supportrip.core.exchange.service;

import com.supportrip.core.account.domain.ForeignAccountTransaction;
import com.supportrip.core.account.domain.ForeignCurrencyWallet;
import com.supportrip.core.account.repository.ForeignAccountRepository;
import com.supportrip.core.account.repository.ForeignAccountTransactionRepository;
import com.supportrip.core.account.service.ForeignAccountService;
import com.supportrip.core.account.service.LinkedAccountService;
import com.supportrip.core.account.service.PointWalletService;
import com.supportrip.core.airplane.domain.AirplaneCertification;
import com.supportrip.core.airplane.service.AirplaneService;
import com.supportrip.core.exchange.domain.*;
import com.supportrip.core.exchange.dto.response.CreateExchangeTradingRequest;
import com.supportrip.core.exchange.dto.response.ExchangeTradingResponse;
import com.supportrip.core.exchange.exception.CountryNotFoundException;
import com.supportrip.core.exchange.exception.CurrencyNotFoundException;
import com.supportrip.core.exchange.exception.ExchangeAccessDeniedException;
import com.supportrip.core.exchange.exception.ExchangeRateNotFoundException;
import com.supportrip.core.exchange.repository.CountryRepository;
import com.supportrip.core.exchange.repository.CurrencyRepository;
import com.supportrip.core.exchange.repository.ExchangeRateRepository;
import com.supportrip.core.exchange.repository.ExchangeTradingRepository;
import com.supportrip.core.user.domain.User;
import com.supportrip.core.user.exception.UserNotFoundException;
import com.supportrip.core.user.repository.UserRepository;
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

    @Transactional
    public void exchange(ExchangeTrading exchangeTrading, Long amount) {
        Currency targetCurrency = exchangeTrading.getTargetCurrency();
        User user = exchangeTrading.getUser();

        ExchangeRate latestExchangeRate = exchangeRateService.getLatestExchangeRate(targetCurrency);
        long fromAmount = calculateAmountForExchange(amount, latestExchangeRate);

        exchangeTrading.reduceAmount(fromAmount);

        ForeignCurrencyWallet foreignCurrencyWallet = foreignAccountService.getForeignCurrencyWallet(user, targetCurrency);
        foreignCurrencyWallet.deposit(amount);

        ForeignAccountTransaction foreignAccountTransaction = ForeignAccountTransaction.of(
                amount,
                latestExchangeRate.getDealBaseRate(),
                foreignCurrencyWallet.getTotalAmount(),
                foreignCurrencyWallet,
                exchangeTrading
        );

        foreignAccountTransactionRepository.save(foreignAccountTransaction);
    }

    private static long calculateAmountForExchange(Long toAmount, ExchangeRate latestExchangeRate) {
        return (long) ((toAmount / latestExchangeRate.getTargetCurrencyUnit()) * latestExchangeRate.getDealBaseRate());
    }

    public List<ExchangeTradingResponse> getInProgressExchangeTradings(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        if(foreignAccountRepository.findByUser(user).isEmpty()) {
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
    public Long createExchangeTrading(Long userId, CreateExchangeTradingRequest request) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        if(foreignAccountRepository.findByUser(user).isEmpty()) {
            throw new ExchangeAccessDeniedException();
        }

        linkedAccountService.withdrawMoney(user, request.getTradingAmount());

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

        return newExchangeTrading.getId();
    }
}
