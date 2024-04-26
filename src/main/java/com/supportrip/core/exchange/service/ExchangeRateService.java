package com.supportrip.core.exchange.service;

import com.supportrip.core.exchange.domain.Currency;
import com.supportrip.core.exchange.domain.ExchangeRate;
import com.supportrip.core.exchange.dto.CurrentExchangeRateResponse;
import com.supportrip.core.exchange.exception.CurrencyNotFoundException;
import com.supportrip.core.exchange.exception.ExchangeRateNotFoundException;
import com.supportrip.core.exchange.exception.OutdatedExchangeRateException;
import com.supportrip.core.exchange.repository.CurrencyRepository;
import com.supportrip.core.exchange.repository.ExchangeRateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class ExchangeRateService {
    private final CurrencyRepository currencyRepository;
    private final ExchangeRateRepository exchangeRateRepository;

    public CurrentExchangeRateResponse getCurrentExchangeRate(Long currencyId) {
        Currency currency = currencyRepository.findById(currencyId)
                .orElseThrow(CurrencyNotFoundException::new);
        ExchangeRate latestExchangeRate = getLatestExchangeRate(currency);

        return CurrentExchangeRateResponse.from(latestExchangeRate);
    }

    public ExchangeRate getLatestExchangeRate(Currency targetCurrency) {
        ExchangeRate exchangeRate = exchangeRateRepository.findLatestExchange(targetCurrency)
                .orElseThrow(ExchangeRateNotFoundException::new);

        LocalDate today = LocalDate.now();
        if (!exchangeRate.isValid(today)) {
            throw new OutdatedExchangeRateException();
        }

        return exchangeRate;
    }
}