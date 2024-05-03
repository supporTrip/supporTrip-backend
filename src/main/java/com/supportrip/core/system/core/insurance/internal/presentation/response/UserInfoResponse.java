package com.supportrip.core.system.core.insurance.internal.presentation.response;

import com.supportrip.core.system.core.user.internal.domain.Gender;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;


@Getter
public class UserInfoResponse {
    private String name;
    private Gender gender;
    private LocalDate birthDay;

    @Builder(access = AccessLevel.PRIVATE)
    private UserInfoResponse(String name, Gender gender, LocalDate birthDay) {
        this.name = name;
        this.gender = gender;
        this.birthDay = birthDay;
    }

    @Builder
    public static UserInfoResponse of(String name, Gender gender, LocalDate birthDay) {
        return UserInfoResponse.builder()
                .name(name)
                .gender(gender)
                .birthDay(birthDay)
                .build();
    }
}
