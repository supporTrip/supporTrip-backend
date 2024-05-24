package com.supportrip.core.system.core.insurance.internal.presentation;

import com.supportrip.core.system.common.internal.SimpleIdResponse;
import com.supportrip.core.system.core.insurance.internal.application.FlightInsuranceService;
import com.supportrip.core.system.core.insurance.internal.domain.FlightInsurance;
import com.supportrip.core.system.core.insurance.internal.presentation.request.AdminFlightInsuranceRequest;
import com.supportrip.core.system.core.insurance.internal.presentation.response.AdminFlightInsuranceResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AdminFlightInsuranceController {
    private final FlightInsuranceService flightInsuranceService;

    @GetMapping("/api/v1/admin/flight-insurances")
    public List<AdminFlightInsuranceResponse> searchFlightInsurance() {
        return flightInsuranceService.findFlightInsurances();
    }

    @GetMapping("/api/v1/admin/flight-insurances/{id}")
    public AdminFlightInsuranceResponse getFlightInsuranceDetail(@PathVariable("id") Long flightInsuranceId) {
        return flightInsuranceService.findInsurance(flightInsuranceId);
    }

    @PostMapping("/api/v1/admin/flight-insurances")
    public SimpleIdResponse createFlightInsurance(@Valid @RequestBody AdminFlightInsuranceRequest request) {
        FlightInsurance flightInsurance = flightInsuranceService.create(request);
        return SimpleIdResponse.from(flightInsurance.getId());
    }

    @PutMapping("/api/v1/admin/flight-insurances")
    public AdminFlightInsuranceResponse updateFlightInsurance(@Valid @RequestBody AdminFlightInsuranceRequest request) {
        return flightInsuranceService.update(request);
    }

    @DeleteMapping("/api/v1/admin/flight-insurances/{id}")
    public void deleteFlightInsurance(@PathVariable("id") Long flightInsuranceId) {
        flightInsuranceService.delete(flightInsuranceId);
    }
}
