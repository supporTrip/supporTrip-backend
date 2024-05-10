package com.supportrip.core.system.core.exchange.internal.application;

import com.supportrip.core.system.core.account.internal.domain.PointWallet;
import com.supportrip.core.system.core.account.internal.application.PointWalletService;
import com.supportrip.core.system.common.external.SmsService;
import com.supportrip.core.system.core.exchange.internal.domain.Currency;
import com.supportrip.core.system.core.exchange.internal.domain.ExchangeRate;
import com.supportrip.core.system.core.exchange.internal.domain.ExchangeTrading;
import com.supportrip.core.system.core.user.internal.domain.User;
import com.supportrip.core.system.core.user.internal.domain.UserNotificationStatus;
import com.supportrip.core.system.core.user.internal.domain.UserNotificationStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static com.supportrip.core.system.core.exchange.internal.domain.TradingStrategy.TARGET;

@Service
@RequiredArgsConstructor
public class TargetExchangeStrategyService implements ExchangeStrategyService {
    private final ExchangeService exchangeService;
    private final PointWalletService pointWalletService;
    private final ExchangeRateService exchangeRateService;
    private final SmsService smsService;
    private final UserNotificationStatusRepository userNotificationStatusRepository;

    @Override
    @Transactional
    public void execute(ExchangeTrading exchangeTrading, LocalDate today) {
        Currency targetCurrency = exchangeTrading.getTargetCurrency();
        ExchangeRate exchangeRate = exchangeRateService.getLatestExchangeRate(targetCurrency);

        if (exchangeTrading.isLastDate(today)) {
            exchangeAllAmount(exchangeTrading, exchangeRate);

            PointWallet pointWallet = pointWalletService.getPointWallet(exchangeTrading.getUser());
            pointWalletService.depositPoint(pointWallet, exchangeTrading.flushCurrentAmount());
            exchangeTrading.changeToComplete();

            sendSmsToUser(exchangeTrading);
            return;
        }

        double targetExchangeRate = exchangeTrading.getTargetExchangeRate();
        if (targetExchangeRate >= exchangeRate.getDealBaseRate()) {
            exchangeAllAmount(exchangeTrading, exchangeRate);
        }
    }

    @Override
    public boolean canApply(ExchangeTrading exchangeTrading) {
        return TARGET.equals(exchangeTrading.getStrategy());
    }

    private void sendSmsToUser(ExchangeTrading exchangeTrading) {
        User user = exchangeTrading.getUser();
        UserNotificationStatus userNotificationStatus = userNotificationStatusRepository.findByUser(user);
        if (userNotificationStatus.getStatus()) {
            smsService.sendOne(makeSmsMessage(exchangeTrading), exchangeTrading.getUser().getSmsPhoneNumber());
        }
    }

    private void exchangeAllAmount(ExchangeTrading exchangeTrading, ExchangeRate exchangeRate) {
        long maxExchangeableAmount = exchangeTrading.getMaxExchangeableCurrencyAmount(exchangeRate.getDealBaseRate());
        exchangeService.exchange(exchangeTrading, maxExchangeableAmount);
    }

    private String makeSmsMessage(ExchangeTrading exchangeTrading) {
        return "[서포트립] " + exchangeTrading.getUser().getName() + "님의 '"
                + exchangeTrading.getDisplayName() + "' 여행을 위한 환전 거래가 완료되었어요!";
    }
}
