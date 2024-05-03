package com.supportrip.core.system.core.exchange.internal.application;

import com.supportrip.core.system.core.exchange.internal.domain.CurrencyRepository;
import com.supportrip.core.system.core.exchange.internal.domain.ExchangeRateRepository;
import com.supportrip.core.system.core.exchange.external.koreaexim.KoreaExImExchangeRateClient;
import com.supportrip.core.system.core.exchange.external.koreaexim.KoreaExImExchangeRateMapper;
import com.supportrip.core.system.core.exchange.external.koreaexim.response.KoreaExImExchangeRateResponse;
import com.supportrip.core.system.core.exchange.internal.domain.Currency;
import com.supportrip.core.system.core.exchange.internal.domain.ExchangeRate;
import com.supportrip.core.system.core.exchange.internal.presentation.response.CurrentExchangeRateResponse;
import com.supportrip.core.context.error.exception.notfound.CurrencyNotFoundException;
import com.supportrip.core.context.error.exception.internalservererror.ExchangeRateDataFetchException;
import com.supportrip.core.context.error.exception.notfound.ExchangeRateNotFoundException;
import com.supportrip.core.context.error.exception.internalservererror.OutdatedExchangeRateException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExchangeRateService {
    private final ExchangeRateRepository exchangeRateRepository;
    private final CurrencyRepository currencyRepository;
    private final ExchangeRateInnerService exchangeRateInnerService;

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
            exchangeRateInnerService.fetchAndStoreExchangeRate(today);

            ExchangeRate renewExchangeRate = exchangeRateRepository.findLatestExchange(targetCurrency)
                    .orElseThrow(ExchangeRateNotFoundException::new);
            if (!renewExchangeRate.isValid(today)) {
                throw new OutdatedExchangeRateException();
            }
        }

        return exchangeRate;
    }

    @Service
    @RequiredArgsConstructor
    public static class ExchangeRateInnerService {
        private final ExchangeRateRepository exchangeRateRepository;
        private final KoreaExImExchangeRateClient exchangeRateClient;
        private final KoreaExImExchangeRateMapper exchangeRateMapper;

        @Transactional
        public void fetchAndStoreExchangeRate(LocalDate searchDate) {
            List<KoreaExImExchangeRateResponse> responses = exchangeRateClient.fetchExchangeRate(searchDate);

            if (responses.isEmpty()) {
                throw new ExchangeRateDataFetchException();
            }

            List<ExchangeRate> exchangeRates = responses.stream()
                    .map(response -> {
                        try {
                            return exchangeRateMapper.convertEntityFrom(response, searchDate);
                        } catch (CurrencyNotFoundException exception) {
                            log.info("{} currency not supported.", response.getCurUnit());
                        }
                        return null;
                    })
                    .filter(Objects::nonNull)
                    .toList();

            exchangeRateRepository.saveAll(exchangeRates);
        }
    }
}