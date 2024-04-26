package com.supportrip.core.user.controller;

import com.supportrip.core.account.dto.response.PointTransactionListResponse;
import com.supportrip.core.auth.domain.OidcUser;
import com.supportrip.core.common.SimpleIdResponse;
import com.supportrip.core.user.domain.User;
import com.supportrip.core.user.dto.request.UserModifiyRequest;
import com.supportrip.core.user.dto.response.MyPageProfileResponse;
import com.supportrip.core.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/mypages")
public class MyPageController {

    private final UserService userService;

    @GetMapping("")
    public MyPageProfileResponse getUserProfile(@AuthenticationPrincipal OidcUser oidcUser) {
        User user = userService.getUser(oidcUser.getUserId());
        return userService.getUserProfile(user);
    }


    @PatchMapping("")
    public SimpleIdResponse modifiyUserProfile(@AuthenticationPrincipal OidcUser oidcUser, @RequestBody UserModifiyRequest request){
        User user = userService.getUser(oidcUser.getUserId());
        return userService.modifiyUserProfile(user, request);
    }

    @GetMapping("/points")
    public PointTransactionListResponse getPointList(@AuthenticationPrincipal OidcUser oidcUser){
        User user = userService.getUser(oidcUser.getUserId());
        return userService.getPointList(user);
    }
}
