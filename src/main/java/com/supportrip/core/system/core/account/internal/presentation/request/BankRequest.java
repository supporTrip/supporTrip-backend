package com.supportrip.core.system.core.account.internal.presentation.request;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class BankRequest {
    private String bankCode;
    private String accountNum;
}
