package com.supportrip.core.user.dto.admin;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
public class AdminUserEnabledUpdateRequest {
    private Long id;
    private boolean enabled;

    @Builder(access = AccessLevel.PRIVATE)
    private AdminUserEnabledUpdateRequest(Long id, boolean enabled) {
        this.id = id;
        this.enabled = enabled;
    }

}
