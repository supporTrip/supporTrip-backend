package com.supportrip.core.system.core.account.internal.presentation.request;

import lombok.Getter;

@Getter
public class GenerateForeignAccountRequest {
    private String bankName;
    private String accountNumber;
}
