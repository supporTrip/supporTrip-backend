package com.supportrip.core.log.repository;

import com.supportrip.core.log.domain.UserLog;
import com.supportrip.core.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserLogRepository extends JpaRepository<UserLog, Long> {
    List<UserLog> findByUserOrderByCreatedAt(User user);
}
