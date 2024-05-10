package com.supportrip.core.system.core.user.internal.presentation.request;

import com.supportrip.core.system.core.account.internal.presentation.request.BankRequest;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class UserModifiyRequest {
    private String phoneNumber;
    private BankRequest bankAccounts;
    private String email;
    private String receiveStatus;
}
