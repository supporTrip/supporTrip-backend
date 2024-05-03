package com.supportrip.core.system.core.account.internal.presentation.response;

import com.supportrip.core.system.core.account.internal.domain.ForeignAccountTransaction;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
public class ForeignAccountTransactionDetailResponse {
    private final String date;
    private final String time;
    private final Double exchangeRate;
    private final Long transactionMoney;
    private final Long totalMoney;

    @Builder(access = AccessLevel.PRIVATE)
    private ForeignAccountTransactionDetailResponse(String date, String time, Double exchangeRate, Long transactionMoney, Long totalMoney) {
        this.date = date;
        this.time = time;
        this.exchangeRate = exchangeRate;
        this.transactionMoney = transactionMoney;
        this.totalMoney = totalMoney;
    }

    public static ForeignAccountTransactionDetailResponse of(ForeignAccountTransaction foreignAccountTransaction) {
        LocalDateTime createdAt = foreignAccountTransaction.getCreatedAt();

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yy.MM.dd");
        String date = createdAt.format(dateFormatter);
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm a");
        String time = createdAt.format(timeFormatter);

        return ForeignAccountTransactionDetailResponse.builder()
                .date(date)
                .time(time)
                .exchangeRate(foreignAccountTransaction.getTargetExchangeRate())
                .transactionMoney(foreignAccountTransaction.getAmount())
                .totalMoney(foreignAccountTransaction.getTargetCurrencyTotalAmount())
                .build();
    }
}
