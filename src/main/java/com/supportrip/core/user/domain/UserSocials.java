package com.supportrip.core.user.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
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

    @Column(name = "refresh_token")
    private String refreshToken;

    @Builder(access = AccessLevel.PRIVATE)
    private UserSocials(Long id, User user, SocialLoginVender vender, String subject, int nonce, String refreshToken) {
        this.id = id;
        this.user = user;
        this.vender = vender;
        this.subject = subject;
        this.nonce = nonce;
        this.refreshToken = refreshToken;
    }

    public static UserSocials of(User user, SocialLoginVender vender, String subject) {
        return UserSocials.builder()
                .user(user)
                .vender(vender)
                .subject(subject)
                .build();
    }
}
