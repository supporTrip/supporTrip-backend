package com.supportrip.core.system.core.account.internal.domain;

import com.supportrip.core.system.common.internal.BaseEntity;
import com.supportrip.core.system.core.user.internal.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "foreign_account")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ForeignAccount extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bank_id")
    private Bank bank;

    @Column(name = "account_number")
    private String accountNumber;

    @Builder(access = AccessLevel.PRIVATE)
    private ForeignAccount(Long id, User user, Bank bank, String accountNumber) {
        this.id = id;
        this.user = user;
        this.bank = bank;
        this.accountNumber = accountNumber;
    }

    public static ForeignAccount of(User user, Bank bank, String accountNumber){
        return ForeignAccount.builder()
                .user(user)
                .bank(bank)
                .accountNumber(accountNumber)
                .build();
    }
}
