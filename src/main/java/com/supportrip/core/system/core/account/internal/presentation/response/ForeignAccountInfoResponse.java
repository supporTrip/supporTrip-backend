package com.supportrip.core.system.core.account.internal.presentation.response;

import com.supportrip.core.system.core.account.internal.domain.ForeignCurrencyWallet;
import com.supportrip.core.system.core.exchange.internal.domain.Country;
import com.supportrip.core.system.core.exchange.internal.domain.Currency;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class ForeignAccountInfoResponse {
    private final String flag;
    private final String name;
    private final String sign;
    private final String unitName;
    private final String totalAmount;
    private final String averageRate;
    private final List<ForeignAccountTransactionDetailResponse> details;

    @Builder(access = AccessLevel.PRIVATE)
    private ForeignAccountInfoResponse(String flag, String name, String sign, String unitName, String totalAmount, String averageRate, List<ForeignAccountTransactionDetailResponse> details) {
        this.flag = flag;
        this.name = name;
        this.sign = sign;
        this.unitName = unitName;
        this.totalAmount = totalAmount;
        this.averageRate = averageRate;
        this.details = details;
    }

    public static ForeignAccountInfoResponse of(ForeignCurrencyWallet foreignCurrencyWallet, Country country, Double averageRate, List<ForeignAccountTransactionDetailResponse> details){
        Currency currency = country.getCurrency();
        return ForeignAccountInfoResponse.builder()
                .flag(country.getFlagUrl())
                .name(country.getCurrency_name())
                .sign(currency.getSign())
                .unitName(currency.getCode())
                .totalAmount(foreignCurrencyWallet.getTotalAmount().toString())
                .averageRate(String.format("%,.2f", averageRate))
                .details(details)
                .build();
    }
}
