package com.supportrip.core.log.domain;


import com.supportrip.core.common.BaseEntity;
import com.supportrip.core.user.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "user_log")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserLog extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private String message;

    @Builder(access = AccessLevel.PRIVATE)
    private UserLog(Long id, User user, String message) {
        this.id = id;
        this.user = user;
        this.message = message;
    }

    public static UserLog of(User user, String message) {
        return UserLog.builder()
                .user(user)
                .message(message)
                .build();
    }
}
