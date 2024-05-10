package com.supportrip.core.system.core.userlog.internal.domain;

import com.supportrip.core.system.core.user.internal.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserLogRepository extends JpaRepository<UserLog, Long> {
    List<UserLog> findByUserOrderByCreatedAt(User user);
}
