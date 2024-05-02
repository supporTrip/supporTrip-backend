package com.supportrip.core.insurance.controller;

import com.supportrip.core.auth.domain.OidcUser;
import com.supportrip.core.common.SimpleIdResponse;
import com.supportrip.core.insurance.domain.FlightInsurance;
import com.supportrip.core.insurance.dto.AdminFlightInsuranceRequest;
import com.supportrip.core.insurance.dto.AdminFlightInsuranceResponse;
import com.supportrip.core.insurance.service.FlightInsuranceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AdminFlightInsuranceController {
    private final FlightInsuranceService flightInsuranceService;

    @GetMapping("/api/v1/admin/flight-insurances")
    public List<AdminFlightInsuranceResponse> searchFlightInsurance(@AuthenticationPrincipal OidcUser oidcUser) {
        return flightInsuranceService.findFlightInsurances(oidcUser.getUserId());
    }

    @GetMapping("/api/v1/admin/flight-insurances/{id}")
    public AdminFlightInsuranceResponse getFlightInsuranceDetail(@AuthenticationPrincipal OidcUser oidcUser,
                                                @PathVariable("id") Long flightInsuranceId) {
        return flightInsuranceService.findInsurance(oidcUser.getUserId(), flightInsuranceId);
    }

    @PostMapping("/api/v1/admin/flight-insurances")
    public SimpleIdResponse createFlightInsurance(@AuthenticationPrincipal OidcUser oidcUser,
                                                  @Valid @RequestBody AdminFlightInsuranceRequest request) {
        FlightInsurance flightInsurance = flightInsuranceService.create(oidcUser.getUserId(), request);
        return SimpleIdResponse.from(flightInsurance.getId());
    }

    @PutMapping("/api/v1/admin/flight-insurances")
    public AdminFlightInsuranceResponse updateFlightInsurance(@AuthenticationPrincipal OidcUser oidcUser,
                                                              @Valid @RequestBody AdminFlightInsuranceRequest request) {
        return flightInsuranceService.update(oidcUser.getUserId(), request);
    }

    @DeleteMapping("/api/v1/admin/flight-insurances/{id}")
    public void deleteFlightInsurance(@PathVariable("id") Long flightInsuranceId, @AuthenticationPrincipal OidcUser oidcUser) {
        flightInsuranceService.delete(oidcUser.getUserId(), flightInsuranceId);
    }
}
