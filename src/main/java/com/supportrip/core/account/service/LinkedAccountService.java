package com.supportrip.core.account.service;

import com.supportrip.core.account.domain.*;
import com.supportrip.core.account.exception.LinkedAccountNotFoundException;
import com.supportrip.core.account.exception.NotEnoughBalanceException;
import com.supportrip.core.account.repository.*;
import com.supportrip.core.user.domain.User;
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
