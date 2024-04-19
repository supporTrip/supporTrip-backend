package com.supportrip.core.insurance.controller;

import com.supportrip.core.insurance.dto.FlightInsuranceDetailRequest;
import com.supportrip.core.insurance.dto.FlightInsuranceDetailResponse;
import com.supportrip.core.insurance.dto.SearchFlightInsuranceRequest;
import com.supportrip.core.insurance.dto.SearchFlightInsuranceResponse;
import com.supportrip.core.insurance.service.FlightInsuranceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

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
}
