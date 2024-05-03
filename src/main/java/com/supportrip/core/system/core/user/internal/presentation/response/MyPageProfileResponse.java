package com.supportrip.core.system.core.user.internal.presentation.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
public class MyPageProfileResponse {
    private final String profilePic;
    private final String name;
    private final String email;
    private final String birthDate;
    private final String gender;
    private final String registrationDate;
    private final String phoneNumber;
    private final String bankAccount;
    private final boolean receiveStatus;

    @Builder(access = AccessLevel.PRIVATE)

    public MyPageProfileResponse(String profilePic, String name, String email, String birthDate, String gender, String registrationDate, String phoneNumber, String bankAccount, boolean receiveStatus) {
        this.profilePic = profilePic;
        this.name = name;
        this.email = email;
        this.birthDate = birthDate;
        this.gender = gender;
        this.registrationDate = registrationDate;
        this.phoneNumber = phoneNumber;
        this.bankAccount = bankAccount;
        this.receiveStatus = receiveStatus;
    }

    public static MyPageProfileResponse of(String profilePic, String name, String email, String birthDate, String gender, String registrationDate, String phoneNumber, String bankAccount, boolean receiveStatus){
        return MyPageProfileResponse.builder()
                .profilePic(profilePic)
                .name(name)
                .email(email)
                .birthDate(birthDate)
                .gender(gender)
                .registrationDate(registrationDate)
                .phoneNumber(phoneNumber)
                .bankAccount(bankAccount)
                .receiveStatus(receiveStatus)
                .build();

    }
}
