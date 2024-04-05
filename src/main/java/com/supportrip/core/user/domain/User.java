package com.supportrip.core.user.domain;

import com.supportrip.core.auth.domain.Role;
import com.supportrip.core.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "email")
    private String email;

    @Column(name = "gender")
    private String gender;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "birth_day")
    private LocalDate birthDay;

    @Column(name = "role")
    private Role role;

    @Column(name = "point")
    private int point;

    @AttributeOverride(name = "created_at", column = @Column(name = "joined_at"))
    private LocalDateTime joinedAt;

    @Column(name = "profile_image_url")
    private String profileImageUrl;

    @Column(name = "pin_number")
    private String pinNumber;

    @Builder
    public User(Long id, String name, String email, String gender, String phoneNumber, LocalDate birthDay, Role role, int point, LocalDateTime joinedAt, String profileImageUrl, String pinNumber) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
        this.birthDay = birthDay;
        this.role = role;
        this.point = point;
        this.joinedAt = joinedAt;
        this.profileImageUrl = profileImageUrl;
        this.pinNumber = pinNumber;
    }
}
