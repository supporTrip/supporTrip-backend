package com.supportrip.core.system.core.account.internal.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "bank")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Bank {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "code", unique = true)
    private String code;

    @Column(name = "bank_image_url")
    private String bankImageUrl;

    @Builder(access = AccessLevel.PRIVATE)
    private Bank(Long id, String name, String code, String bankImageUrl) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.bankImageUrl = bankImageUrl;
    }

    public static Bank of(String name, String code, String bankImageUrl) {
        return Bank.builder()
                .name(name)
                .code(code)
                .bankImageUrl(bankImageUrl)
                .build();
    }
}
