package com.supportrip.core.exchange.scheduler;

import com.supportrip.core.exchange.domain.Currency;
import com.supportrip.core.exchange.domain.ExchangeRate;
import com.supportrip.core.exchange.exception.CurrencyNotFoundException;
import com.supportrip.core.exchange.repository.CurrencyRepository;
import com.supportrip.core.exchange.scheduler.dto.KoreaExImExchangeRateResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KoreaExImExchangeRateMapper {
    private static final String KOREA_WON_CURRENCY_UNIT = "KRW";

    private final CurrencyRepository currencyRepository;

    public ExchangeRate convertEntityFrom(KoreaExImExchangeRateResponse response) {
        Currency baseCurrency = currencyRepository.findByCode(KOREA_WON_CURRENCY_UNIT)
                .orElseThrow(CurrencyNotFoundException::new);

        String currencyUnit = getCurrencyUnit(response.getCurUnit());
        Currency targetCurrency = currencyRepository.findByCode(currencyUnit)
                .orElseThrow(CurrencyNotFoundException::new);

        Long targetCurrencyUnit = getTargetCurrencyUnit(response.getCurUnit());
        double dealBasR = getDealBasR(response);

        return ExchangeRate.of(targetCurrency, targetCurrencyUnit, baseCurrency, dealBasR);
    }

    private static double getDealBasR(KoreaExImExchangeRateResponse response) {
        String dealBasR = response.getDealBasR().replace(",", "");
        return Double.valueOf(dealBasR);
    }

    private static String getCurrencyUnit(String curUnit) {
        if (curUnit.length() > 3) {
            return curUnit.substring(0, 3);
        }
        return curUnit;
    }

    private static Long getTargetCurrencyUnit(String curUnit) {
        if (curUnit.length() > 3) {
            String targetCurrencyUnit = curUnit.substring(4, curUnit.length() - 1);
            return Long.valueOf(targetCurrencyUnit);
        }
        return 1L;
    }
}
