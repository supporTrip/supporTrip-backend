package com.supportrip.core.system.core.user.internal.presentation.response;

import com.supportrip.core.system.core.account.internal.domain.PointWallet;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
public class CurrentUserPointResponse {
    private final long point;

    @Builder(access = AccessLevel.PRIVATE)
    public CurrentUserPointResponse(long point) {
        this.point = point;
    }

    public static CurrentUserPointResponse from(PointWallet pointWallet) {
        return CurrentUserPointResponse.builder()
                .point(pointWallet.getTotalAmount())
                .build();
    }
}
