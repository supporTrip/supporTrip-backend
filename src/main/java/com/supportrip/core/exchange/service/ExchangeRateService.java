package com.supportrip.core.exchange.service;

import com.supportrip.core.exchange.domain.Currency;
import com.supportrip.core.exchange.domain.ExchangeRate;
import com.supportrip.core.exchange.dto.response.CurrentExchangeRateResponse;
import com.supportrip.core.exchange.exception.CurrencyNotFoundException;
import com.supportrip.core.exchange.exception.ExchangeRateNotFoundException;
import com.supportrip.core.exchange.exception.OutdatedExchangeRateException;
import com.supportrip.core.exchange.repository.CurrencyRepository;
import com.supportrip.core.exchange.repository.ExchangeRateRepository;
import com.supportrip.core.exchange.scheduler.KoreaExImExchangeRateClient;
import com.supportrip.core.exchange.scheduler.KoreaExImExchangeRateMapper;
import com.supportrip.core.exchange.scheduler.dto.KoreaExImExchangeRateResponse;
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
                throw new IllegalStateException("오늘은 수출입은행으로부터 환율 정보를 가져올 수 없는 일자입니다. 비영업일이거나 영업 시간 이전일 수 있습니다.");
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