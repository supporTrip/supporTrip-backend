package com.supportrip.core.user.repository;

import com.supportrip.core.user.domain.PhoneVerification;
import com.supportrip.core.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PhoneVerificationRepository extends JpaRepository<PhoneVerification, Long> {
    Optional<PhoneVerification> findByUser(User user);
}
