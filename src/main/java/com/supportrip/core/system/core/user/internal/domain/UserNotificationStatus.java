package com.supportrip.core.system.core.user.internal.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "user_notification_status")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserNotificationStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "status")
    private boolean status;

    @Builder(access = AccessLevel.PRIVATE)
    public UserNotificationStatus(Long id, User user, boolean status) {
        this.id = id;
        this.user = user;
        this.status = status;
    }

    public static UserNotificationStatus of(User user, boolean status){
        return UserNotificationStatus.builder()
                .user(user)
                .status(status)
                .build();
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public boolean getStatus(){
        return this.status;
    }
}
