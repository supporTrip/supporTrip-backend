package com.supportrip.core.account.service;

import com.supportrip.core.account.domain.LinkedAccount;
import com.supportrip.core.account.domain.PointTransaction;
import com.supportrip.core.account.domain.PointTransactionType;
import com.supportrip.core.account.domain.PointWallet;
import com.supportrip.core.account.exception.LinkedAccountNotFoundException;
import com.supportrip.core.account.exception.PointWalletNotFoundException;
import com.supportrip.core.account.repository.LinkedAccountRepository;
import com.supportrip.core.account.repository.PointTransactionRepository;
import com.supportrip.core.account.repository.PointWalletRepository;
import com.supportrip.core.exchange.exception.NotEnoughPointException;
import com.supportrip.core.user.domain.User;
import com.supportrip.core.user.exception.UserNotFoundException;
import com.supportrip.core.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PointWalletService {
    private final UserRepository userRepository;
    private final PointWalletRepository pointWalletRepository;
    private final PointTransactionRepository pointTransactionRepository;
    private final LinkedAccountRepository linkedAccountRepository;

    public PointWallet getPointWallet(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        return getPointWallet(user);
    }

    public PointWallet getPointWallet(User user) {
        return pointWalletRepository.findByUser(user).orElseThrow(PointWalletNotFoundException::new);
    }

    public List<PointTransaction> getPointTransactions(User user) {
        PointWallet pointWallet = getPointWallet(user);
        return pointTransactionRepository.findByPointWalletOrderByCreatedAtDesc(pointWallet);
    }

    @Transactional
    public void withdrawPoint(User user, Long point) {
        PointWallet pointWallet = getPointWallet(user);

        Long totalAmount = pointWallet.getTotalAmount();
        if (totalAmount - point < 0) {
            throw new NotEnoughPointException();
        }

        Long pointBalance = totalAmount - point;
        LinkedAccount linkedAccount = linkedAccountRepository.findByUser(user).orElseThrow(LinkedAccountNotFoundException::new);

        pointWallet.reduce(point);
        pointTransactionRepository.save(PointTransaction.of(pointWallet,linkedAccount,point, PointTransactionType.WITHDRAWAL,pointBalance));
    }
}
