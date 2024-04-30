package com.supportrip.core.account.dto.request;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class BankRequest {
    private String bankCode;
    private String accountNum;
}
