package com.supportrip.core.account.domain;

import com.supportrip.core.common.BaseEntity;
import com.supportrip.core.user.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "foreign_account")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ForeignAccount extends BaseEntity {
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

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "total_usd")
    private Double totalUSD;

    @Column(name = "total_jpy")
    private Double totalJPY;

    @Column(name = "total_eur")
    private Double totalEUR;

    @Column(name = "total_gbp")
    private Double totalGBP;

    @Column(name = "total_cad")
    private Double totalCAD;

    @Column(name = "total_aud")
    private Double totalAUD;

    @Column(name = "total_nzd")
    private Double totalNZD;

    @Column(name = "total_sgd")
    private Double totalSGD;

    @Column(name = "total_hkd")
    private Double totalHKD;

    @Column(name = "total_cny")
    private Double totalCNY;

    @Column(name = "total_chf")
    private Double totalCHF;

    @Column(name = "total_myr")
    private Double totalMYR;

    @Column(name = "total_php")
    private Double totalPHP;

    @Column(name = "total_idr")
    private Double totalIDR;

    @Column(name = "total_thb")
    private Double totalTHB;

    @Column(name = "total_vnd")
    private Double totalVND;

    @Column(name = "total_twd")
    private Double totalTWD;

    @Builder(access = AccessLevel.PRIVATE)
    private ForeignAccount(Long id, User user, Bank bank, String accountNumber, LocalDateTime createdAt, Double totalUSD, Double totalJPY, Double totalEUR, Double totalGBP, Double totalCAD, Double totalAUD, Double totalNZD, Double totalSGD, Double totalHKD, Double totalCNY, Double totalCHF, Double totalMYR, Double totalPHP, Double totalIDR, Double totalTHB, Double totalVND, Double totalTWD) {
        this.id = id;
        this.user = user;
        this.bank = bank;
        this.accountNumber = accountNumber;
        this.createdAt = createdAt;
        this.totalUSD = totalUSD;
        this.totalJPY = totalJPY;
        this.totalEUR = totalEUR;
        this.totalGBP = totalGBP;
        this.totalCAD = totalCAD;
        this.totalAUD = totalAUD;
        this.totalNZD = totalNZD;
        this.totalSGD = totalSGD;
        this.totalHKD = totalHKD;
        this.totalCNY = totalCNY;
        this.totalCHF = totalCHF;
        this.totalMYR = totalMYR;
        this.totalPHP = totalPHP;
        this.totalIDR = totalIDR;
        this.totalTHB = totalTHB;
        this.totalVND = totalVND;
        this.totalTWD = totalTWD;
    }

    public static ForeignAccount of(User user, Bank bank, String accountNumber){
        return ForeignAccount.builder()
                .user(user)
                .bank(bank)
                .accountNumber(accountNumber)
                .totalUSD(0.0)
                .totalJPY(0.0)
                .totalEUR(0.0)
                .totalGBP(0.0)
                .totalCAD(0.0)
                .totalAUD(0.0)
                .totalNZD(0.0)
                .totalSGD(0.0)
                .totalHKD(0.0)
                .totalCNY(0.0)
                .totalCHF(0.0)
                .totalMYR(0.0)
                .totalPHP(0.0)
                .totalIDR(0.0)
                .totalTHB(0.0)
                .totalVND(0.0)
                .totalTWD(0.0)
                .build();
    }
}
