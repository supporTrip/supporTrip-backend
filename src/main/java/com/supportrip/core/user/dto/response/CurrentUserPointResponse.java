package com.supportrip.core.user.dto.response;

import com.supportrip.core.account.domain.PointWallet;
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
