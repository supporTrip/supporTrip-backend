package com.supportrip.core.account.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class ForeignAccountInfoListResponse {
    private final Boolean hasAccount;
    private final List<ForeignAccountInfoResponse> accountInfo;

    @Builder(access = AccessLevel.PRIVATE)
    private ForeignAccountInfoListResponse(Boolean hasAccount, List<ForeignAccountInfoResponse> accountInfo) {
        this.hasAccount = hasAccount;
        this.accountInfo = accountInfo;
    }

    public static ForeignAccountInfoListResponse of(Boolean hasAccount, List<ForeignAccountInfoResponse> foreignAccountInfoResponseList){
        return ForeignAccountInfoListResponse.builder()
                .hasAccount(hasAccount)
                .accountInfo(foreignAccountInfoResponseList)
                .build();
    }
}
