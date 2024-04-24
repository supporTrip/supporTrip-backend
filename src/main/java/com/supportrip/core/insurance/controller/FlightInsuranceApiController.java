package com.supportrip.core.insurance.controller;

import com.supportrip.core.auth.domain.OidcUser;
import com.supportrip.core.common.SimpleIdResponse;
import com.supportrip.core.insurance.domain.InsuranceSubscription;
import com.supportrip.core.insurance.dto.*;
import com.supportrip.core.insurance.service.FlightInsuranceService;
import com.supportrip.core.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class FlightInsuranceApiController {
    private final FlightInsuranceService flightInsuranceService;
    private final UserService userService;

    @GetMapping("/api/v1/flight-insurances/search")
    public ResponseEntity<List<SearchFlightInsuranceResponse>> searchFlightInsurance(@Valid SearchFlightInsuranceRequest request) {
        List<SearchFlightInsuranceResponse> flightInsurances = flightInsuranceService.findFlightInsuranceFilter(request);

        return ResponseEntity.ok(flightInsurances);
    }

    @GetMapping("/api/v1/flight-insurance/{id}")
    public ResponseEntity<FlightInsuranceDetailResponse> detail(@PathVariable("id") Long flightInsuranceId, @Valid FlightInsuranceDetailRequest request) {
        FlightInsuranceDetailResponse flightInsuranceDetail = flightInsuranceService.findFlightInsuranceDetail(flightInsuranceId, request);

        return ResponseEntity.ok(flightInsuranceDetail);
    }

    @PostMapping("/api/v1/flight-insurance-subscriptions")
    public ResponseEntity<SimpleIdResponse> insuranceSubscription(@AuthenticationPrincipal OidcUser oidcUser,
                                                                  @Valid @RequestBody SubscriptionRequest request) {
        InsuranceSubscription insuranceSubscription = flightInsuranceService.insuranceSubscription(oidcUser.getUserId(), request);
        SimpleIdResponse response = SimpleIdResponse.from(insuranceSubscription.getId());
        return ResponseEntity.ok(response);
    }
}
