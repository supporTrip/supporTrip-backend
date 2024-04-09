package com.supportrip.core.insurance.controller;

import com.supportrip.core.insurance.dto.SearchFlightInsuranceRequestDTO;
import com.supportrip.core.insurance.dto.SearchFlightInsuranceResponseDTO;
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

    @GetMapping("/api/v1/search")
    public ResponseEntity<List<SearchFlightInsuranceResponseDTO>> searchFlightInsurance(@Valid SearchFlightInsuranceRequestDTO requestDTO) {
        List<SearchFlightInsuranceResponseDTO> flightInsurances = flightInsuranceService.findFlightInsuranceFilter(requestDTO);

        return ResponseEntity.ok(flightInsurances);
    }
}
