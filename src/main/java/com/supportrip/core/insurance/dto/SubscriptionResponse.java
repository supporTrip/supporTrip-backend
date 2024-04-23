package com.supportrip.core.insurance.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
public class SubscriptionResponse {
    private final Long id;

    @Builder(access = AccessLevel.PRIVATE)
    private SubscriptionResponse(Long id) {
        this.id = id;
    }

    public static SubscriptionResponse from(Long id) {
        return new SubscriptionResponse(id);
    }
}
