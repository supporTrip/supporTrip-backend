package com.supportrip.core.account.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ForeignAccountResponse {
    private final Long foreignAccountId;

    @Builder(access = AccessLevel.PRIVATE)
    private ForeignAccountResponse(Long foreignAccountId) {
        this.foreignAccountId = foreignAccountId;
    }

    public static ForeignAccountResponse of(Long foreignAccountId){
        return ForeignAccountResponse.builder()
                .foreignAccountId(foreignAccountId)
                .build();
    }
}
