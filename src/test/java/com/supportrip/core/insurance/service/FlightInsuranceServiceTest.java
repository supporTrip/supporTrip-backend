package com.supportrip.core.insurance.service;

import com.supportrip.core.insurance.domain.FlightInsurance;
import com.supportrip.core.insurance.domain.InsuranceCompany;
import com.supportrip.core.insurance.domain.SpecialContract;
import com.supportrip.core.insurance.dto.SearchFlightInsuranceRequest;
import com.supportrip.core.insurance.dto.SearchFlightInsuranceResponse;
import com.supportrip.core.insurance.repository.FlightInsuranceRepository;
import com.supportrip.core.insurance.repository.SpecialContractRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FlightInsuranceServiceTest {

    @Mock //목객체 생성
    private FlightInsuranceRepository flightInsuranceRepository;

    @Mock
    private FlightInsuranceCalculatePremiumService calculatePremiumService;

    @Mock
    private SpecialContractRepository specialContractRepository;

    @InjectMocks //의존성 주입
    private FlightInsuranceService flightInsuranceService;

    @Test
    @DisplayName("필터링 보험 검색 조회")
    void findFlightInsuranceFilter() {

        InsuranceCompany insuranceCompany1 = InsuranceCompany.from("한화생명");
        InsuranceCompany insuranceCompany2 = InsuranceCompany.from("삼성생명");

        List<FlightInsurance> mockFlightInsurances = Arrays.asList(
                FlightInsurance.of(insuranceCompany1, "한화생명 해외여행자 보험", 1000, 15, 60, true, true, true),
                FlightInsurance.of(insuranceCompany2, "삼성생명 해외여행자 보험", 2000, 15, 60, true, true, false)
        );

        List<SpecialContract> mockSpecialContracts1 = Arrays.asList(
                SpecialContract.of(mockFlightInsurances.get(0), "해외의료보험", 1000, 2000, 3000),
                SpecialContract.of(mockFlightInsurances.get(0), "항공기지연", 1000, 2000, 3000),
                SpecialContract.of(mockFlightInsurances.get(0), "휴대폰분실", 1000, 2000, 3000)
        );

        List<SpecialContract> mockSpecialContracts2 = Arrays.asList(
                SpecialContract.of(mockFlightInsurances.get(1), "해외의료보험", 2000, 3000, 4000),
                SpecialContract.of(mockFlightInsurances.get(1), "항공기지연", 2000, 3000, 4000),
                SpecialContract.of(mockFlightInsurances.get(1), "휴대폰분실", 2000, 3000, 4000)
        );

        List<FlightInsurance> mockCalPremium = Arrays.asList(
                mockFlightInsurances.get(0)
        );
        List<FlightInsurance> mockCalExpected = Arrays.asList(
                FlightInsurance.of(insuranceCompany1, "한화생명 해외여행자 보험", 4440, 15, 60, true, true, true)
        );

        when(flightInsuranceRepository.findByAge(eq(25)))
                .thenReturn(mockFlightInsurances);
        when(calculatePremiumService.calculatePremium(eq(25), eq(4), eq("standard"), eq("male"), eq(mockCalPremium)))
                .thenReturn(mockCalExpected);

        SearchFlightInsuranceRequest request = SearchFlightInsuranceRequest.of(
                LocalDateTime.of(2024, 4, 8, 10, 0),
                LocalDateTime.of(2024, 4, 12, 18, 0),
                LocalDate.of(1998, 5, 10),
                "male",
                "standard",
                true,
                true,
                true
        );

        List<SearchFlightInsuranceResponse> result = flightInsuranceService.findFlightInsuranceFilter(request);

        assertNotNull(result);
        assertEquals(1, result.size());
    }
}