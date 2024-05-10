package com.supportrip.core.system.core.insurance.internal.application;

import com.supportrip.core.system.core.insurance.internal.domain.*;
import com.supportrip.core.system.core.insurance.internal.presentation.request.SearchFlightInsuranceRequest;
import com.supportrip.core.system.core.insurance.internal.presentation.request.SubscriptionRequest;
import com.supportrip.core.system.core.insurance.internal.presentation.response.SearchFlightInsuranceResponse;
import com.supportrip.core.system.core.mydata.external.InsuranceClient;
import com.supportrip.core.system.core.user.internal.domain.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@MockitoSettings
class FlightInsuranceServiceTest {

    @Mock //목객체 생성
    private FlightInsuranceRepository flightInsuranceRepository;

    @Mock
    private FlightInsuranceCalculatePremiumService calculatePremiumService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private InsuranceSubscriptionRepository subscriptionRepository;

    @Mock
    private SpecialContractRepository specialContractRepository;

    @Mock
    private UserCIRepository userCIRepository;

    @Mock
    private InsuranceClient insuranceClient;

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
                SpecialContract.of(mockFlightInsurances.get(0), "해외의료보험", 1000, 2000),
                SpecialContract.of(mockFlightInsurances.get(0), "항공기지연", 1000, 2000),
                SpecialContract.of(mockFlightInsurances.get(0), "휴대폰분실", 1000, 2000)
        );

        List<SpecialContract> mockSpecialContracts2 = Arrays.asList(
                SpecialContract.of(mockFlightInsurances.get(1), "해외의료보험", 2000, 3000),
                SpecialContract.of(mockFlightInsurances.get(1), "항공기지연", 2000, 3000),
                SpecialContract.of(mockFlightInsurances.get(1), "휴대폰분실", 2000, 3000)
        );

        FlightInsurance mockCalExpected = FlightInsurance.of(insuranceCompany1, "한화생명 해외여행자 보험", 4440, 15, 60, true, true, true);

        when(flightInsuranceRepository.findByAge(eq(25)))
                .thenReturn(mockFlightInsurances);

        SearchFlightInsuranceRequest request = SearchFlightInsuranceRequest.of(
                LocalDateTime.of(2024, 4, 8, 10, 0),
                LocalDateTime.of(2024, 4, 12, 18, 0),
                LocalDate.of(1998, 5, 10),
                Gender.MALE,
                "standard",
                true,
                true,
                true
        );

        List<SearchFlightInsuranceResponse> result = flightInsuranceService.findFlightInsuranceFilter(request);

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    @DisplayName("보험신청이력 생성 테스트")
    void insuranceSubscription() {
        Long userId = 1L;
        User user = User.initialUserOf("profile_img");
        SubscriptionRequest request = SubscriptionRequest.of(
                1L, // 보험 ID 설정
                LocalDateTime.now().plusDays(1), // 보장 시작일 설정
                LocalDateTime.now().plusDays(10), // 보장 종료일 설정
                30000, // 보험료 설정
                true, // 보장 내용 동의 설정
                true // 개인정보 동의 설정
        );

        InsuranceCompany name = InsuranceCompany.from("삼성생명");

        FlightInsurance flightInsurance = FlightInsurance.of(
                name,
                "해외여행자 보험",
                3000,
                15,
                70,
                true,
                true,
                true
        );

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(flightInsuranceRepository.findById(request.getFlightInsuranceId())).thenReturn(Optional.of(flightInsurance));
        when(userCIRepository.findByUser(user)).thenReturn(UserCI.of(user, "1234"));

        // When
        InsuranceSubscription insuranceSubscription = flightInsuranceService.insuranceSubscription(userId, request);

        // Then
        verify(subscriptionRepository, times(1)).save(any(InsuranceSubscription.class));
        assertEquals(insuranceSubscription.getTotalPremium(), 30000);
        assertNotNull(insuranceSubscription);
    }
}