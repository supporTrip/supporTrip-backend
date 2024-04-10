package com.supportrip.core.user.repository;

import com.supportrip.core.user.domain.SocialLoginVender;
import com.supportrip.core.user.domain.UserSocials;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface UserSocialsRepository extends JpaRepository<UserSocials, Long> {
    Optional<UserSocials> findByVenderAndSubject(SocialLoginVender vender, String subject);
}
