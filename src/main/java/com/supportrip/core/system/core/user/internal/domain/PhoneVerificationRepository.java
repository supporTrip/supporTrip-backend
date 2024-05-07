package com.supportrip.core.system.core.user.internal.domain;

import com.supportrip.core.system.core.user.internal.domain.PhoneVerification;
import com.supportrip.core.system.core.user.internal.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PhoneVerificationRepository extends JpaRepository<PhoneVerification, Long> {
    Optional<PhoneVerification> findByUser(User user);
}
