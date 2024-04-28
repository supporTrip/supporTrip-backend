package com.supportrip.core.user.repository;

import com.supportrip.core.user.domain.User;
import com.supportrip.core.user.domain.UserCI;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserCIRepository extends JpaRepository<UserCI, Long> {
    UserCI findByUser(User user);
}
