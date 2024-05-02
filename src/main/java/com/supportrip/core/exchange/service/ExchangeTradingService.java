package com.supportrip.core.exchange.service;

import com.supportrip.core.account.domain.ForeignAccountTransaction;
import com.supportrip.core.account.repository.ForeignAccountTransactionRepository;
import com.supportrip.core.exchange.domain.ExchangeTrading;
import com.supportrip.core.exchange.dto.response.ExchangeTransactionListResponse;
import com.supportrip.core.exchange.dto.response.ExchangeTransactionResponse;
import com.supportrip.core.exchange.repository.ExchangeTradingRepository;
import com.supportrip.core.user.domain.User;
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
