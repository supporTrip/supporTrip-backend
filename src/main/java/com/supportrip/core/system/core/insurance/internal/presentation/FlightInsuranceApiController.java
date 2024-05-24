package com.supportrip.core.system.core.insurance.internal.presentation;

import com.supportrip.core.system.common.internal.SimpleIdResponse;
import com.supportrip.core.system.core.auth.internal.domain.OidcUser;
import com.supportrip.core.system.core.insurance.internal.application.FlightInsuranceService;
import com.supportrip.core.system.core.insurance.internal.application.InsuranceService;
import com.supportrip.core.system.core.insurance.internal.domain.InsuranceSubscription;
import com.supportrip.core.system.core.insurance.internal.presentation.request.FlightInsuranceDetailRequest;
import com.supportrip.core.system.core.insurance.internal.presentation.request.SearchFlightInsuranceRequest;
import com.supportrip.core.system.core.insurance.internal.presentation.request.SubscriptionRequest;
import com.supportrip.core.system.core.insurance.internal.presentation.response.FlightInsuranceDetailResponse;
import com.supportrip.core.system.core.insurance.internal.presentation.response.RecomandInsuranceListResponse;
import com.supportrip.core.system.core.insurance.internal.presentation.response.SearchFlightInsuranceResponse;
import com.supportrip.core.system.core.user.internal.application.UserService;
import com.supportrip.core.system.core.user.internal.domain.User;
import com.supportrip.core.system.core.userlog.internal.application.UserLogService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class FlightInsuranceApiController {
    private final FlightInsuranceService flightInsuranceService;
    private final UserLogService userLogService;
    private final InsuranceService insuranceService;

    @GetMapping("/api/v1/flight-insurances/search")
    public ResponseEntity<List<SearchFlightInsuranceResponse>> searchFlightInsurance(@Valid SearchFlightInsuranceRequest request,
                                                                                     @AuthenticationPrincipal OidcUser oidcUser) {
        List<SearchFlightInsuranceResponse> flightInsurances = flightInsuranceService.findFlightInsuranceFilter(request);

        if (oidcUser == null) {
            userLogService.appendAnonymousUserLog("Anonymous User searched with " + request);
        } else {
            Long userId = oidcUser.getUser().getId();
            userLogService.appendUserLog(userId, "User[ID=" + userId + "] searched with " + request);
        }

        return ResponseEntity.ok(flightInsurances);
    }

    @GetMapping("/api/v1/flight-insurances/{id}")
    public ResponseEntity<FlightInsuranceDetailResponse> detail(@PathVariable("id") Long flightInsuranceId,
                                                                @Valid FlightInsuranceDetailRequest request,
                                                                @AuthenticationPrincipal OidcUser oidcUser) {
        FlightInsuranceDetailResponse flightInsuranceDetail = flightInsuranceService.findFlightInsuranceDetail(flightInsuranceId, request);

        if (oidcUser == null) {
            userLogService.appendAnonymousUserLog("Anonymous User searched with " + request);
        } else {
            Long userId = oidcUser.getUser().getId();
            userLogService.appendUserLog(userId, "User[ID=" + userId + "] looked up FlightInsurance[ID=" + flightInsuranceId + "]");
        }

        return ResponseEntity.ok(flightInsuranceDetail);
    }

    @PostMapping("/api/v1/flight-insurance-subscriptions")
    public ResponseEntity<SimpleIdResponse> insuranceSubscription(@AuthenticationPrincipal OidcUser oidcUser,
                                                                  @Valid @RequestBody SubscriptionRequest request) {
        InsuranceSubscription insuranceSubscription = flightInsuranceService.insuranceSubscription(oidcUser.getUser(), request);
        Long userId = oidcUser.getUser().getId();
        userLogService.appendUserLog(userId,
                "User[ID=" + userId + "] subscribe FlightInsurance[ID=" + request.getFlightInsuranceId() + "]");

        SimpleIdResponse response = SimpleIdResponse.from(insuranceSubscription.getId());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/v1/flight-insurances/recomands")
    public RecomandInsuranceListResponse getRecomandInsuranceList(@AuthenticationPrincipal OidcUser oidcUser,
                                                                  @RequestParam(name = "departAt") LocalDateTime departAt,
                                                                  @RequestParam(name = "arrivalAt") LocalDateTime arrivalAt) {
        return insuranceService.getRecomandInsuranceList(oidcUser.getUser(), departAt, arrivalAt);
    }
}
