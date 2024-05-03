package com.supportrip.core.system.core.user.internal.domain;

import com.supportrip.core.system.core.user.internal.domain.SocialLoginVender;
import com.supportrip.core.system.core.user.internal.domain.UserSocials;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserSocialsRepository extends JpaRepository<UserSocials, Long> {
    Optional<UserSocials> findByVenderAndSubject(SocialLoginVender vender, String subject);

    Optional<UserSocials> findByVenderAndUserIdAndRefreshToken(SocialLoginVender vender, Long userId, String refreshToken);

    Optional<UserSocials> findByVenderAndUserId(SocialLoginVender vender, Long userId);
}
