package com.supportrip.core.system.core.insurance.internal.presentation;

import com.supportrip.core.system.core.auth.internal.domain.OidcUser;
import com.supportrip.core.system.common.internal.SimpleIdResponse;
import com.supportrip.core.system.core.insurance.internal.domain.FlightInsurance;
import com.supportrip.core.system.core.insurance.internal.application.FlightInsuranceService;
import com.supportrip.core.system.core.insurance.internal.presentation.request.AdminFlightInsuranceRequest;
import com.supportrip.core.system.core.insurance.internal.presentation.response.AdminFlightInsuranceResponse;
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
