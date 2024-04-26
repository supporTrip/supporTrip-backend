package com.supportrip.core.user.repository;

import com.supportrip.core.user.domain.User;
import com.supportrip.core.user.domain.UserNotificationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserNotificationStatusRepository extends JpaRepository<UserNotificationStatus, Long> {
    UserNotificationStatus findByUser(User user);
}
