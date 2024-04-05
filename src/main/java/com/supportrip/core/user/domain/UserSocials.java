package com.supportrip.core.user.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "user_socials")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserSocials {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "vender")
    @Enumerated(EnumType.STRING)
    private SocialLoginVender vender;

    @Column(name = "subject")
    private String subject;

    @Column(name = "nonce")
    private int nonce;

    public UserSocials(Long id, User user, SocialLoginVender vender, String subject, int nonce) {
        this.id = id;
        this.user = user;
        this.vender = vender;
        this.subject = subject;
        this.nonce = nonce;
    }
}
