package com.supportrip.core.account.dto.request;

import lombok.Getter;

@Getter
public class GenerateForeignAccountRequest {
    private String bankName;
    private String accountNumber;
}
