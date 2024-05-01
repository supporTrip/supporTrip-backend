package com.supportrip.core.airplane.controller;

import com.supportrip.core.airplane.dto.request.CertificatePnrNumberRequest;
import com.supportrip.core.airplane.dto.response.CertificatePnrNumberResponse;
import com.supportrip.core.airplane.service.AirplaneService;
import com.supportrip.core.auth.domain.OidcUser;
import com.supportrip.core.log.service.UserLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/airplain")
public class AirplaneController {
    private final AirplaneService airplaneService;
    private final UserLogService userLogService;

    @PostMapping("/certification")
    public CertificatePnrNumberResponse certificatePnrNumber(@AuthenticationPrincipal OidcUser oidcUser,
                                                             @RequestBody CertificatePnrNumberRequest request) {
        CertificatePnrNumberResponse response = airplaneService.certificatePnrNumber(request);
        userLogService.appendUserLog(
                oidcUser.getUserId(),
                "User[ID=" + oidcUser.getUserId() + "] authenticated flight ticket with PNR number."
        );
        return response;
    }

}
