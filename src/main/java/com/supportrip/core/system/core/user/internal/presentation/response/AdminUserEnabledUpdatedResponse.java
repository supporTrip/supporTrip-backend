package com.supportrip.core.system.core.user.internal.presentation.response;

import com.supportrip.core.system.core.user.internal.domain.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
public class AdminUserEnabledUpdatedResponse {
    private Long id;
    private boolean enabled;

    @Builder(access = AccessLevel.PRIVATE)
    private AdminUserEnabledUpdatedResponse(Long id, boolean enabled) {
        this.id = id;
        this.enabled = enabled;
    }

    public static AdminUserEnabledUpdatedResponse of (User user) {
        return AdminUserEnabledUpdatedResponse.builder()
                .id(user.getId())
                .enabled(user.isEnabled())
                .build();
    }
}
