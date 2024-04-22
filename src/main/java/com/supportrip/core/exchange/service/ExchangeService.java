package com.supportrip.core.exchange.service;

import com.supportrip.core.account.repository.ForeignAccountRepository;
import com.supportrip.core.exchange.domain.Currency;
import com.supportrip.core.exchange.domain.ExchangeTrading;
import com.supportrip.core.exchange.exception.ExchangeAccessDeniedException;
import com.supportrip.core.exchange.repository.CurrencyRepository;
import com.supportrip.core.exchange.repository.ExchangeTradingRepository;
import com.supportrip.core.user.domain.User;
import com.supportrip.core.user.exception.UserNotFoundException;
import com.supportrip.core.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExchangeService {
    private final ExchangeTradingRepository exchangeTradingRepository;
    private final UserRepository userRepository;
    private final CurrencyRepository currencyRepository;
    private final ForeignAccountRepository foreignAccountRepository;

    public List<ExchangeTrading> getInProgressExchangeTradings(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        if(foreignAccountRepository.findByUser(user).isEmpty()) {
            throw new ExchangeAccessDeniedException();
        }

        return exchangeTradingRepository.findByUserAndCompletedAtIsNull(user);
    }

    public List<Currency> getExchangeableCurrency() {
        return currencyRepository.findAll();
    }
}
