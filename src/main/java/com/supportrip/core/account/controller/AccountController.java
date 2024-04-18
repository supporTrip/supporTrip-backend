package com.supportrip.core.account.controller;

import com.supportrip.core.account.dto.request.GenerateForeignAccountRequest;
import com.supportrip.core.account.dto.response.GenerateForeignAccountResponse;
import com.supportrip.core.account.service.AccountService;
import com.supportrip.core.auth.domain.OidcUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/accounts")
public class AccountController {
    private final AccountService accountService;

    @PostMapping("/foreign")
    public GenerateForeignAccountResponse generateForeignAccount(@AuthenticationPrincipal OidcUser oidcUser
            , @RequestBody GenerateForeignAccountRequest generateForeignAccountRequest){
        return accountService.generateForeignAccount(oidcUser.getUserId(), generateForeignAccountRequest);
    }
}