package com.supportrip.core.user.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "user_ci")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserCI {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "token")
    private String token;

    @Builder(access = AccessLevel.PRIVATE)

    public UserCI(Long id, User user, String token) {
        this.id = id;
        this.user = user;
        this.token = token;
    }

    public static UserCI of(User user, String token){
        return UserCI.builder()
                .user(user)
                .token(token)
                .build();
    }
}
