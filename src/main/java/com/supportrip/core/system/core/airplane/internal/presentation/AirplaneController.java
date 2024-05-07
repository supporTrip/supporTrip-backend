package com.supportrip.core.system.core.airplane.internal.presentation;

import com.supportrip.core.system.core.airplane.internal.presentation.request.CertificatePnrNumberRequest;
import com.supportrip.core.system.core.airplane.internal.presentation.response.CertificatePnrNumberResponse;
import com.supportrip.core.system.core.airplane.internal.application.AirplaneService;
import com.supportrip.core.system.core.auth.internal.domain.OidcUser;
import com.supportrip.core.system.core.userlog.internal.application.UserLogService;
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
