package com.supportrip.core.system.core.exchange.external.koreaexim;

import com.supportrip.core.system.core.exchange.external.koreaexim.response.KoreaExImExchangeRateResponse;
import com.supportrip.core.system.core.exchange.internal.domain.Currency;
import com.supportrip.core.system.core.exchange.internal.domain.ExchangeRate;
import com.supportrip.core.context.error.exception.notfound.CurrencyNotFoundException;
import com.supportrip.core.system.core.exchange.internal.domain.CurrencyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class KoreaExImExchangeRateMapper {
    private static final String KOREA_WON_CURRENCY_UNIT = "KRW";

    private final CurrencyRepository currencyRepository;

    public ExchangeRate convertEntityFrom(KoreaExImExchangeRateResponse response, LocalDate date) {
        Currency baseCurrency = currencyRepository.findByCode(KOREA_WON_CURRENCY_UNIT)
                .orElseThrow(CurrencyNotFoundException::new);

        String currencyUnit = getCurrencyUnit(response.getCurUnit());
        Currency targetCurrency = currencyRepository.findByCode(currencyUnit)
                .orElseThrow(CurrencyNotFoundException::new);

        Long targetCurrencyUnit = getTargetCurrencyUnit(response.getCurUnit());
        double dealBasR = getDealBasR(response);

        return ExchangeRate.of(date, targetCurrency, targetCurrencyUnit, baseCurrency, dealBasR);
    }

    private static double getDealBasR(KoreaExImExchangeRateResponse response) {
        String dealBasR = response.getDealBasR().replace(",", "");
        return Double.parseDouble(dealBasR);
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
