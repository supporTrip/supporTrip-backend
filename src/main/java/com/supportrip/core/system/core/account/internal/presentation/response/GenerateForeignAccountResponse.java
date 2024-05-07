package com.supportrip.core.system.core.account.internal.presentation.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
public class GenerateForeignAccountResponse {
    private final Long foreignAccountId;

    @Builder(access = AccessLevel.PRIVATE)
    private GenerateForeignAccountResponse(Long foreignAccountId) {
        this.foreignAccountId = foreignAccountId;
    }

    public static GenerateForeignAccountResponse of(Long foreignAccountId){
        return GenerateForeignAccountResponse.builder()
                .foreignAccountId(foreignAccountId)
                .build();
    }
}
