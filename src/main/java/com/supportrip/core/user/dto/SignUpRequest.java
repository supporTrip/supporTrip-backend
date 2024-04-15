package com.supportrip.core.user.dto;

import com.supportrip.core.user.domain.Gender;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class SignUpRequest {
    @NotBlank(message = "이름을 입력해주세요.")
    private String name;

    @NotBlank(message = "이메일을 입력해주세요.")
    private String email;

    @NotBlank(message = "휴대폰 번호를 입력해주세요.")
    private String phoneNumber;

    @NotNull(message = "생년월일을 입력해주세요.")
    private LocalDate birthDay;

    @NotNull(message = "성별을 선택해주세요.")
    private Gender gender;

    @NotNull(message = "핀번호를 입력해주세요.")
    private String pinNumber;

    @Builder(access = AccessLevel.PRIVATE)
    public SignUpRequest(String name, String email, String phoneNumber, LocalDate birthDay, Gender gender, String pinNumber) {
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.birthDay = birthDay;
        this.gender = gender;
        this.pinNumber = pinNumber;
    }

    public static SignUpRequest of(String name, String email, String phoneNumber, LocalDate birthDay, Gender gender, String pinNumber) {
        return new SignUpRequest(name, email, phoneNumber, birthDay, gender, pinNumber);
    }
}
