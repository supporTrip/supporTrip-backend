package com.supportrip.core.account.dto.request;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ForeignAccountRequest {
    private String bankName;
    private String accountNumber;
}
