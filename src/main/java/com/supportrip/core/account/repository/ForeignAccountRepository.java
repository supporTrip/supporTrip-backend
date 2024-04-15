package com.supportrip.core.account.repository;

import com.supportrip.core.account.domain.ForeignAccount;
import com.supportrip.core.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ForeignAccountRepository extends JpaRepository<ForeignAccount, Long> {
    ForeignAccount findByUser(User user);
}
