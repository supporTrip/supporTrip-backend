package com.supportrip.core.system.core.user.internal.domain;

import com.supportrip.core.system.core.user.internal.domain.User;
import com.supportrip.core.system.core.user.internal.domain.UserNotificationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserNotificationStatusRepository extends JpaRepository<UserNotificationStatus, Long> {
    UserNotificationStatus findByUser(User user);
}
