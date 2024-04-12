package com.supportrip.core.user.repository;

import com.supportrip.core.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
