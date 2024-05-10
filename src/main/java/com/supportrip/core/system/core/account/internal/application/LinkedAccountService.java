package com.supportrip.core.system.core.account.internal.application;

import com.supportrip.core.context.error.exception.badrequest.NotEnoughBalanceException;
import com.supportrip.core.context.error.exception.notfound.LinkedAccountNotFoundException;
import com.supportrip.core.system.core.account.internal.domain.LinkedAccount;
import com.supportrip.core.system.core.account.internal.domain.LinkedAccountRepository;
import com.supportrip.core.system.core.user.internal.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LinkedAccountService {
    private final LinkedAccountRepository linkedAccountRepository;

    @Transactional
    public void withdrawMoney(User user, Long money) {
        LinkedAccount linkedAccount = linkedAccountRepository.findByUser(user).orElseThrow(LinkedAccountNotFoundException::new);

        Long totalAmount = linkedAccount.getTotalAmount();
        if (totalAmount - money < 0) {
            throw new NotEnoughBalanceException();
        }

        totalAmount -= money;
        linkedAccount.setTotalAmount(totalAmount);
    }

}
