package com.supportrip.core.system.core.user.internal.domain;

import com.supportrip.core.context.error.exception.badrequest.PhoneVerificationFailException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "phone_verification")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PhoneVerification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "user_id")
    @OneToOne(fetch = FetchType.LAZY)
    private User user;

    @Column(name = "code")
    private String code;

    @Column(name = "expires_in")
    private LocalDateTime expiresIn;

    @Column(name = "verified_at")
    private LocalDateTime verifiedAt;

    @Builder(access = AccessLevel.PRIVATE)
    private PhoneVerification(Long id, User user, String code, LocalDateTime expiresIn, LocalDateTime verifiedAt) {
        this.id = id;
        this.user = user;
        this.code = code;
        this.expiresIn = expiresIn;
        this.verifiedAt = verifiedAt;
    }

    public static PhoneVerification of(User user, String code, LocalDateTime expiresIn) {
        return PhoneVerification.builder()
                .user(user)
                .code(code)
                .expiresIn(expiresIn)
                .build();
    }

    public void renew(String code, LocalDateTime expiresIn) {
        this.code = code;
        this.expiresIn = expiresIn;
    }

    public boolean isVerified() {
        return this.verifiedAt != null;
    }

    public void verify(String code) {
        LocalDateTime now = LocalDateTime.now();
        if (expiresIn.isBefore(now) || !this.code.equals(code)) {
            throw new PhoneVerificationFailException();
        }

        this.verifiedAt = LocalDateTime.now();
    }
}
