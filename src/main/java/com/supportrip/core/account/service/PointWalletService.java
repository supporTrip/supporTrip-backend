package com.supportrip.core.account.service;

import com.supportrip.core.account.domain.PointTransaction;
import com.supportrip.core.account.domain.PointWallet;
import com.supportrip.core.account.exception.PointWalletNotFoundException;
import com.supportrip.core.account.repository.PointTransactionRepository;
import com.supportrip.core.account.repository.PointWalletRepository;
import com.supportrip.core.user.domain.User;
import com.supportrip.core.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PointWalletService {
    private final UserService userService;
    private final PointWalletRepository pointWalletRepository;
    private final PointTransactionRepository pointTransactionRepository;

    public PointWallet getPointWallet(Long userId) {
        User user = userService.getUser(userId);
        return getPointWallet(user);
    }

    public PointWallet getPointWallet(User user) {
        return pointWalletRepository.findByUser(user).orElseThrow(PointWalletNotFoundException::new);
    }

    public List<PointTransaction> getPointTransactions(User user) {
        PointWallet pointWallet = getPointWallet(user);
        return pointTransactionRepository.findByPointWalletOrderByCreatedAtDesc(pointWallet);
    }
}
