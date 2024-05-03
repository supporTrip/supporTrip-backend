package com.supportrip.core.system.core.exchange.internal.application;

import com.supportrip.core.system.core.account.internal.domain.ForeignAccountTransaction;
import com.supportrip.core.system.core.account.internal.domain.ForeignAccountTransactionRepository;
import com.supportrip.core.system.core.exchange.internal.domain.ExchangeTradingRepository;
import com.supportrip.core.system.core.exchange.internal.domain.ExchangeTrading;
import com.supportrip.core.system.core.exchange.internal.presentation.response.ExchangeTransactionListResponse;
import com.supportrip.core.system.core.exchange.internal.presentation.response.ExchangeTransactionResponse;
import com.supportrip.core.system.core.user.internal.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExchangeTradingService {
    private final ExchangeTradingRepository exchangeTradingRepository;
    private final ForeignAccountTransactionRepository foreignAccountTransactionRepository;

    public ExchangeTransactionListResponse getExchangeList(User user) {
        List<ExchangeTrading> tradings = exchangeTradingRepository.findByUserOrderByCreatedAtDesc(user);
        List<ExchangeTransactionResponse> exchangeTransactionResponses = new ArrayList<>();

        for(ExchangeTrading exchangeTrading : tradings){
            String transactionDate = exchangeTrading.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy.MM.dd"));
            String name = exchangeTrading.getDisplayName();
            Long tradingAmount = exchangeTrading.getTradingAmount();
            String targetCode = exchangeTrading.getTargetCurrency().getCode();

            List<ForeignAccountTransaction> foreignAccountTransactions = foreignAccountTransactionRepository.findByExchangeTrading(exchangeTrading);
            Long totalTargetAmount = 0L;
            Double totalOriginAmount = 0d;
            for(ForeignAccountTransaction foreignAccountTransaction : foreignAccountTransactions) {
                totalTargetAmount += foreignAccountTransaction.getAmount();
                if (foreignAccountTransaction.getTargetExchangeRate() == null) {
                    continue;
                }
                totalOriginAmount += foreignAccountTransaction.getAmount() * foreignAccountTransaction.getTargetExchangeRate();
            }

            String targetAmount = String.format("%,d",totalTargetAmount) + " " + targetCode;

            Double avgRate = totalOriginAmount / totalTargetAmount;
            String targetAvg = String.format("%,.2f", avgRate) + "원";
            exchangeTransactionResponses.add(ExchangeTransactionResponse.of(transactionDate, name, String.format("%,d",tradingAmount)+"원", targetAmount, targetAvg));
        }

        return ExchangeTransactionListResponse.of(exchangeTransactionResponses);
    }
}
