package com.supportrip.core.system.core.account.internal.application;

import com.supportrip.core.context.error.exception.badrequest.NotEnoughPointException;
import com.supportrip.core.context.error.exception.notfound.LinkedAccountNotFoundException;
import com.supportrip.core.context.error.exception.notfound.PointWalletNotFoundException;
import com.supportrip.core.system.core.account.internal.domain.*;
import com.supportrip.core.system.core.user.internal.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PointWalletService {
    private final PointWalletRepository pointWalletRepository;
    private final PointTransactionRepository pointTransactionRepository;
    private final LinkedAccountRepository linkedAccountRepository;

    public PointWallet getPointWallet(User user) {
        return pointWalletRepository.findByUser(user).orElseThrow(PointWalletNotFoundException::new);
    }

    public List<PointTransaction> getPointTransactions(User user) {
        PointWallet pointWallet = getPointWallet(user);
        return pointTransactionRepository.findByPointWalletOrderByCreatedAtDesc(pointWallet);
    }

    @Transactional
    public void withdrawPoint(User user, Long point) {
        if (point <= 0) {
            return;
        }

        PointWallet pointWallet = getPointWallet(user);

        Long totalAmount = pointWallet.getTotalAmount();
        if (totalAmount - point < 0) {
            throw new NotEnoughPointException();
        }

        Long pointBalance = totalAmount - point;
        LinkedAccount linkedAccount = linkedAccountRepository.findByUser(user).orElseThrow(LinkedAccountNotFoundException::new);

        pointWallet.reduce(point);
        pointTransactionRepository.save(PointTransaction.of(pointWallet, linkedAccount, point, PointTransactionType.WITHDRAWAL, pointBalance));
    }

    @Transactional
    public void depositPoint(PointWallet pointWallet, Long point) {
        pointWallet.increase(point);

        PointTransaction pointTransaction =
                PointTransaction.of(pointWallet, null, point, PointTransactionType.DEPOSIT, pointWallet.getTotalAmount());
        pointTransactionRepository.save(pointTransaction);
    }
}
