package com.supportrip.core.system.core.user.internal.domain;

import com.supportrip.core.system.core.user.internal.domain.User;
import com.supportrip.core.system.core.user.internal.domain.UserCI;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserCIRepository extends JpaRepository<UserCI, Long> {
    UserCI findByUser(User user);
}
