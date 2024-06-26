package com.supportrip.core.system.core.user.internal.domain;

import com.supportrip.core.system.core.auth.internal.domain.Role;
import com.supportrip.core.system.common.internal.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Getter
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AttributeOverride(name = "createdAt", column = @Column(name = "joined_at"))
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "email")
    private String email;

    @Column(name = "gender")
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "birth_day")
    private LocalDate birthDay;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "profile_image_url")
    private String profileImageUrl;

    @Column(name = "pin_number")
    private String pinNumber;

    @Column(name = "enabled", nullable = false)
    private boolean enabled;

    @Column(name = "locked_at")
    private LocalDateTime lockedAt;

    @Builder(access = AccessLevel.PRIVATE)
    private User(Long id, String name, String email, Gender gender, String phoneNumber, LocalDate birthDay, Role role, String profileImageUrl, String pinNumber, boolean enabled, LocalDateTime lockedAt) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
        this.birthDay = birthDay;
        this.role = role;
        this.profileImageUrl = profileImageUrl;
        this.pinNumber = pinNumber;
        this.enabled = enabled;
        this.lockedAt = lockedAt;
    }

    public static User initialUserOf(String profileImageUrl) {
        return of(null, null, null, null, null, Role.USER, profileImageUrl);
    }

    public static User userOf(String name, String email, Gender gender, String phoneNumber, LocalDate birthDay, String profileImageUrl) {
        return of(name, email, gender, phoneNumber, birthDay, Role.USER, profileImageUrl);
    }

    private static User of(String name, String email, Gender gender, String phoneNumber, LocalDate birthDay, Role role, String profileImageUrl) {
        return User.builder()
                .name(name)
                .email(email)
                .gender(gender)
                .phoneNumber(phoneNumber)
                .birthDay(birthDay)
                .role(role)
                .profileImageUrl(profileImageUrl)
                .enabled(true)
                .build();
    }

    public void fillInitialUserInfo(String name, String email, Gender gender, String phoneNumber, LocalDate birthDay, String pinNumber) {
        this.name = name;
        this.email = email;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
        this.birthDay = birthDay;
        this.pinNumber = pinNumber;
    }

    public Set<GrantedAuthority> getAuthorities() {
        return Set.of(new SimpleGrantedAuthority(role.toRoleKey()));
    }

    public boolean isLocked() {
        return lockedAt != null;
    }

    public void replacePinNumber(String pinNumber) {
        this.pinNumber = pinNumber;
    }

    public boolean isInitialUser() {
        return name == null;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void enabledUpdate(boolean enabled) {
        this.enabled = enabled;
    }

    public String toLogMessage() {
        return "email='" + email + '\'' + ", phoneNumber='" + phoneNumber + '\'';
    }

    public String getSmsPhoneNumber() {
        return this.phoneNumber.replace("-", "");
    }
}
