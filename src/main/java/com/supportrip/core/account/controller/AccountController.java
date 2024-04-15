package com.supportrip.core.account.controller;

import com.supportrip.core.account.dto.request.ForeignAccountRequest;
import com.supportrip.core.account.dto.response.ForeignAccountResponse;
import com.supportrip.core.account.service.AccountService;
import com.supportrip.core.auth.domain.OidcUser;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/account")
public class AccountController {
    private final AccountService accountService;

    @PostMapping("/foreign")
    public ForeignAccountResponse generateForeignAccount(@AuthenticationPrincipal OidcUser oidcUser
            , @RequestBody ForeignAccountRequest foreignAccountRequest){
        return accountService.generateForeignAccount(oidcUser.getUserId(), foreignAccountRequest);
    }
}
