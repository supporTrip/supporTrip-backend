package com.supportrip.core.insurance.controller;

import com.supportrip.core.insurance.dto.SearchFlightInsuranceRequest;
import com.supportrip.core.insurance.dto.SearchFlightInsuranceResponse;
import com.supportrip.core.insurance.service.FlightInsuranceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class FlightInsuranceApiController {
    private final FlightInsuranceService flightInsuranceService;

    @GetMapping("/api/v1/flight-insurance/search")
    public ResponseEntity<List<SearchFlightInsuranceResponse>> searchFlightInsurance(@Valid SearchFlightInsuranceRequest requestDTO) {
        List<SearchFlightInsuranceResponse> flightInsurances = flightInsuranceService.findFlightInsuranceFilter(requestDTO);

        return ResponseEntity.ok(flightInsurances);
    }
}
