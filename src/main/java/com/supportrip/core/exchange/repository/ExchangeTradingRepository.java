package com.supportrip.core.exchange.repository;

import com.supportrip.core.exchange.domain.ExchangeTrading;
import com.supportrip.core.user.domain.User;
import com.supportrip.core.exchange.domain.TradingStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExchangeTradingRepository extends JpaRepository<ExchangeTrading, Long> {
    List<ExchangeTrading> findByUserAndStatus(User user, TradingStatus status);
    List<ExchangeTrading> findByStatus(TradingStatus status);

    List<ExchangeTrading> findByUserOrderByCreatedAtDesc(User user);
}
