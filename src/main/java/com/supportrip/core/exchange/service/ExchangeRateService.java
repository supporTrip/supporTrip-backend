package com.supportrip.core.exchange.service;

import com.supportrip.core.exchange.domain.Currency;
import com.supportrip.core.exchange.domain.ExchangeRate;
import com.supportrip.core.exchange.exception.ExchangeRateNotFoundException;
import com.supportrip.core.exchange.exception.OutdatedExchangeRateException;
import com.supportrip.core.exchange.repository.ExchangeRateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class ExchangeRateService {
    private final ExchangeRateRepository exchangeRateRepository;

    public ExchangeRate getLatestExchangeRate(Currency targetCurrency) {
        ExchangeRate exchangeRate = exchangeRateRepository.findLatestExchangeByTargetCurrency(targetCurrency)
                .orElseThrow(ExchangeRateNotFoundException::new);

        LocalDate today = LocalDate.now();
        if (!exchangeRate.isValid(today)) {
            throw new OutdatedExchangeRateException();
        }

        return exchangeRate;
    }
}