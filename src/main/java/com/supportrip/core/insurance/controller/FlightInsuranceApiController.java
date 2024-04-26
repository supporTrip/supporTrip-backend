package com.supportrip.core.insurance.controller;

import com.supportrip.core.auth.domain.OidcUser;
import com.supportrip.core.common.SimpleIdResponse;
import com.supportrip.core.insurance.domain.FlightInsurance;
import com.supportrip.core.insurance.domain.InsuranceSubscription;
import com.supportrip.core.insurance.dto.*;
import com.supportrip.core.insurance.service.FlightInsuranceService;
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

    @GetMapping("/admin/v1/flight-insurances")
    public List<AdminFlightInsuranceResponse> adminSearch(@AuthenticationPrincipal OidcUser oidcUser) {
        return flightInsuranceService.findFlightInsurances(oidcUser.getUserId());
    }

    @PostMapping("/admin/v1/flight-insurance")
    public SimpleIdResponse createFlightInsurance(@AuthenticationPrincipal OidcUser oidcUser,
                                      @Valid @RequestBody AdminFlightInsuranceRequest request) {
        FlightInsurance flightInsurance = flightInsuranceService.create(oidcUser.getUserId(), request);
        return SimpleIdResponse.from(flightInsurance.getId());
    }

    @PutMapping("/admin/v1/flight-insurance")
    public AdminFlightInsuranceResponse update (@AuthenticationPrincipal OidcUser oidcUser,
                                                @Valid @RequestBody AdminFlightInsuranceRequest request) {
        return flightInsuranceService.update(oidcUser.getUserId(), request);
    }

    @DeleteMapping("/admin/v1/flight-insurance/{id}")
    public void delete (@PathVariable("id") Long flightInsuranceId, @AuthenticationPrincipal OidcUser oidcUser) {
        flightInsuranceService.delete(oidcUser.getUserId(), flightInsuranceId);
    }
}
