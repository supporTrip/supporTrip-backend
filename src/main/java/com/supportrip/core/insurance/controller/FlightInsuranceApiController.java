package com.supportrip.core.insurance.controller;

import com.supportrip.core.auth.domain.OidcUser;
import com.supportrip.core.insurance.dto.*;
import com.supportrip.core.insurance.service.FlightInsuranceService;
import com.supportrip.core.user.domain.User;
import com.supportrip.core.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping("/api/v1/flight-insurance/user")
    public ResponseEntity<UserInfoResponse> getUserInfo(@AuthenticationPrincipal OidcUser oidcUser) {
        User user = userService.getUser(oidcUser.getUserId());
        return ResponseEntity.ok(UserInfoResponse.of(user.getName(), user.getGender() , user.getBirthDay()));
    }
}
