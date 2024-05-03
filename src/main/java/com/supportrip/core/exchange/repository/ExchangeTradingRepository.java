package com.supportrip.core.exchange.repository;

import com.supportrip.core.exchange.domain.ExchangeTrading;
import com.supportrip.core.exchange.domain.TradingStatus;
import com.supportrip.core.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ExchangeTradingRepository extends JpaRepository<ExchangeTrading, Long> {
    @Query("SELECT e FROM ExchangeTrading e " +
            "WHERE e.user = :user " +
            "AND e.status = :status " +
            "ORDER BY function('datediff', e.completeDate, current_timestamp) ASC")
    List<ExchangeTrading> findByUserAndStatusOrderByDdayAsc(
            @Param("user")  User user,
            @Param("status") TradingStatus status
    );
    List<ExchangeTrading> findByStatus(TradingStatus status);

    List<ExchangeTrading> findByUserOrderByCreatedAtDesc(User user);
}
