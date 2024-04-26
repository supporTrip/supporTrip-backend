package com.supportrip.core.account.domain;

import com.supportrip.core.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "point_transaction")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AttributeOverride(name = "createdAt", column = @Column(name = "transacted_at"))
public class PointTransaction extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "point_wallet_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private PointWallet pointWallet;

    @JoinColumn(name = "linked_account_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private LinkedAccount linkedAccount;

    @Column(name = "amount")
    private Long amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private PointTransactionType type;

    @Column(name = "point_wallet_total_amount")
    private Long totalAmount;

    @Builder(access = AccessLevel.PRIVATE)
    private PointTransaction(Long id, PointWallet pointWallet, LinkedAccount linkedAccount, Long amount, PointTransactionType type, Long totalAmount) {
        this.id = id;
        this.pointWallet = pointWallet;
        this.linkedAccount = linkedAccount;
        this.amount = amount;
        this.type = type;
        this.totalAmount = totalAmount;
    }
}
