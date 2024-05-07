package com.supportrip.core.system.core.account.internal.domain;

import com.supportrip.core.system.core.user.internal.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "linked_account")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LinkedAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bank_id")
    private Bank bank;

    @Column(name = "account_number")
    private String accountNumber;

    @Column(name = "total_amount")
    private Long totalAmount;

    @Builder(access = AccessLevel.PRIVATE)
    private LinkedAccount(Long id, User user, Bank bank, String accountNumber, Long totalAmount) {
        this.id = id;
        this.user = user;
        this.bank = bank;
        this.accountNumber = accountNumber;
        this.totalAmount = totalAmount;
    }

    public static LinkedAccount of(User user, Bank bank, String accountNumber) {
        return LinkedAccount.builder()
                .user(user)
                .bank(bank)
                .totalAmount(1_000_000L) // 계좌에 100만원이 들어있다고 가정
                .accountNumber(accountNumber)
                .build();
    }

    public void setBank(Bank bank) {
        this.bank = bank;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public void setTotalAmount(Long totalAmount) {
        this.totalAmount = totalAmount;
    }
}
