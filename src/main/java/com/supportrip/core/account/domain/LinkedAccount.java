package com.supportrip.core.account.domain;

import com.supportrip.core.user.domain.User;
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

    @Builder(access = AccessLevel.PRIVATE)
    private LinkedAccount(Long id, User user, Bank bank, String accountNumber) {
        this.id = id;
        this.user = user;
        this.bank = bank;
        this.accountNumber = accountNumber;
    }

    public static LinkedAccount of(User user, Bank bank, String accountNumber){
        return LinkedAccount.builder()
                .user(user)
                .bank(bank)
                .accountNumber(accountNumber)
                .build();
    }
}
