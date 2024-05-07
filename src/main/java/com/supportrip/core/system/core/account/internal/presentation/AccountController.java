package com.supportrip.core.system.core.account.internal.presentation;

import com.supportrip.core.system.core.account.internal.presentation.request.GenerateForeignAccountRequest;
import com.supportrip.core.system.core.account.internal.presentation.response.ForeignAccountInfoListResponse;
import com.supportrip.core.system.core.account.internal.presentation.response.GenerateForeignAccountResponse;
import com.supportrip.core.system.core.account.internal.application.AccountService;
import com.supportrip.core.system.core.auth.internal.domain.OidcUser;
import com.supportrip.core.system.core.userlog.internal.application.UserLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/accounts")
public class AccountController {
    private final AccountService accountService;
    private final UserLogService userLogService;

    @PostMapping("/foreign")
    public GenerateForeignAccountResponse generateForeignAccount(@AuthenticationPrincipal OidcUser oidcUser,
                                                                 @RequestBody GenerateForeignAccountRequest request) {
        Long userId = oidcUser.getUserId();
        GenerateForeignAccountResponse response = accountService.generateForeignAccount(userId, request);
        userLogService.appendUserLog(userId, "User[ID=" + userId + "] has created a new foreign currency account.");
        return response;
    }

    @GetMapping("/details")
    public ForeignAccountInfoListResponse getForeignAccountInfo(@AuthenticationPrincipal OidcUser oidcUser) {
        return accountService.getForeignAccountInfo(oidcUser.getUserId());
    }
}
