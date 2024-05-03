package com.supportrip.core.system.core.account.internal.domain;

import com.supportrip.core.context.error.exception.badrequest.NotEnoughPointException;
import com.supportrip.core.system.core.user.internal.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "point_wallet")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PointWallet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "user_id")
    @OneToOne(fetch = FetchType.LAZY)
    private User user;

    @Column(name = "total_amount")
    private Long totalAmount;

    @Builder(access = AccessLevel.PRIVATE)
    private PointWallet(Long id, User user, Long totalAmount) {
        this.id = id;
        this.user = user;
        this.totalAmount = totalAmount;
    }

    public static PointWallet of(User user, Long totalAmount) {
        return PointWallet.builder()
                .user(user)
                .totalAmount(totalAmount)
                .build();
    }

    public boolean hasMoreAmount(long totalAmount) {
        return this.totalAmount >= totalAmount;
    }

    public void reduce(long amount) {
        if (!hasMoreAmount(amount)) {
            throw new NotEnoughPointException();
        }

        this.totalAmount -= amount;
    }

    public void increase(long amount) {
        this.totalAmount += amount;
    }
}
