package com.supportrip.core.account.domain;

import com.supportrip.core.exchange.domain.Currency;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "foreign_currency_wallet")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ForeignCurrencyWallet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "foreign_account_id")
    private ForeignAccount foreignAccount;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "currency_id")
    private Currency currency;

    @Column(name = "country_name")
    private String countryName;

    @Column(name = "total_amount")
    private Double totalAmount;

    @Builder(access = AccessLevel.PRIVATE)
    private ForeignCurrencyWallet(Long id, ForeignAccount foreignAccount, Currency currency, String countryName, Double totalAmount) {
        this.id = id;
        this.foreignAccount = foreignAccount;
        this.currency = currency;
        this.countryName = countryName;
        this.totalAmount = totalAmount;
    }

    public static ForeignCurrencyWallet of(ForeignAccount foreignAccount, Currency currency, Double totalAmount){
        return ForeignCurrencyWallet.builder()
                .foreignAccount(foreignAccount)
                .currency(currency)
                .countryName(currency.getCountry().getName())
                .totalAmount(totalAmount)
                .build();
    }
}
