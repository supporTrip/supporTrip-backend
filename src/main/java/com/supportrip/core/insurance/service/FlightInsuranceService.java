package com.supportrip.core.insurance.service;

import com.supportrip.core.insurance.domain.FlightInsurance;
import com.supportrip.core.insurance.domain.InsuranceSubscription;
import com.supportrip.core.insurance.domain.SpecialContract;
import com.supportrip.core.insurance.dto.*;
import com.supportrip.core.insurance.exception.NotFoundFlightInsuranceException;
import com.supportrip.core.insurance.repository.FlightInsuranceRepository;
import com.supportrip.core.insurance.repository.InsuranceSubscriptionRepository;
import com.supportrip.core.insurance.repository.SpecialContractRepository;
import com.supportrip.core.user.domain.User;
import com.supportrip.core.user.exception.UserNotFoundException;
import com.supportrip.core.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FlightInsuranceService {
    private final FlightInsuranceRepository flightInsuranceRepository;
    private final FlightInsuranceCalculatePremiumService calculatePremiumService;
    private final SpecialContractRepository specialContractRepository;
    private final UserRepository userRepository;
    private final InsuranceSubscriptionRepository subscriptionRepository;

    /**
     * 필터링 검색
     */
    public List<SearchFlightInsuranceResponse> findFlightInsuranceFilter(SearchFlightInsuranceRequest request) {

        int age = calculateAge(request.getBirthDay());
        int period = calculatePeriod(request.getDepartAt(), request.getArrivalAt());

        //연령대가 일치하는 보험상품 모두 조회
        List<FlightInsurance> findFlightInsurances = flightInsuranceRepository.findByAge(age);

        //선택된 카테고리가 포함되어있는 보험상품 필터
        List<FlightInsurance> filteredInsurances = flightInsuranceFilter(findFlightInsurances,
                request.getFlightDelay(),
                request.getPassportLoss(), request.getFoodPoisoning());

        //보험료 계산
        List<FlightInsurance> flightInsurances = calculatePremiumService.calculatePremium(age, period, request.getPlanName(), request.getGender(), filteredInsurances);

        //특약 상위3개 추가
        return addTop3SpecialContract(flightInsurances, request.getPlanName(), request.getDepartAt(), request.getArrivalAt());
    }

    /**
     * 여행자 보험 상품에 특약 상위3개 리스트로 추가
     */
    private List<SearchFlightInsuranceResponse> addTop3SpecialContract(List<FlightInsurance> flightInsurances, String planName, LocalDateTime departAt, LocalDateTime arrivalAt) {
        List<SearchFlightInsuranceResponse> searchFlightInsuranceResponses = new ArrayList<>();

        for (FlightInsurance flightInsurance : flightInsurances) {
            List<SpecialContract> findSpecialContracts = specialContractRepository.findByFlightInsuranceId(flightInsurance.getId(), PageRequest.of(0, 3));
            List<Top3SpecialContractResponse> contractTop3Responses = new ArrayList<>();
            for (SpecialContract findSpecialContract : findSpecialContracts) {
                Top3SpecialContractResponse top3SpecialContractResponse;
                if (planName.equals("standard")) {
                    top3SpecialContractResponse = Top3SpecialContractResponse.standard(findSpecialContract);
                } else {
                    top3SpecialContractResponse = Top3SpecialContractResponse.advanced(findSpecialContract);
                }
                contractTop3Responses.add(top3SpecialContractResponse);
            }
            SearchFlightInsuranceResponse searchFlightInsuranceResponse = SearchFlightInsuranceResponse.toDTO(flightInsurance, contractTop3Responses, planName, departAt, arrivalAt);
            searchFlightInsuranceResponses.add(searchFlightInsuranceResponse);
        }

        return searchFlightInsuranceResponses;
    }

    /**
     * 여행자 보험 특약 카테고리 필터
     */
    private List<FlightInsurance> flightInsuranceFilter(List<FlightInsurance> findFlightInsurance,
                                                        boolean flightDelay, boolean passportLoss, boolean foodPoisoning) {

        List<String> selectedList = new ArrayList<>();
        List<FlightInsurance> newFlightInsurance = new ArrayList<>();

        if (flightDelay) {
            selectedList.add("flightDelay");
        }
        if (passportLoss) {
            selectedList.add("passportLoss");
        }
        if (foodPoisoning) {
            selectedList.add("foodPoisoning");
        }

        for (FlightInsurance flightInsurance : findFlightInsurance) {
            boolean isAllInclude = true;
            for (String s : selectedList) {
                if (s.equals("flightDelay")) {
                    if (!flightInsurance.isFlightDelay()) {
                        isAllInclude = false;
                        break;
                    }
                } else if (s.equals("passportLoss")) {
                    if (!flightInsurance.isPassportLoss()) {
                        isAllInclude = false;
                        break;
                    }
                } else if (s.equals("foodPoisoning")) {
                    if (!flightInsurance.isFoodPoisoning()) {
                        isAllInclude = false;
                        break;
                    }
                }
            }
            if (isAllInclude) {
                newFlightInsurance.add(flightInsurance);
            }
        }
        return newFlightInsurance;
    }

    /**
     * 만 나이 계산
     */
    private int calculateAge(LocalDate birthDay) {

        // 현재시간
        LocalDate currentDate = LocalDate.now();

        // 기간차이
        Period period = Period.between(birthDay, currentDate);

        return period.getYears();
    }

    /**
     * 여행기간 계산
     */
    private int calculatePeriod(LocalDateTime departAt, LocalDateTime arrivalAt) {
        // LocalDateTime to LocalDate
        LocalDate departDate = departAt.toLocalDate();
        LocalDate arrivalDate = arrivalAt.toLocalDate();

        // 기간차이
        Period period = Period.between(departDate, arrivalDate);

        return period.getDays();
    }

    /**
     * 특정 보험상품 세부조회
     */
    public FlightInsuranceDetailResponse findFlightInsuranceDetail(Long flightInsuranceId, FlightInsuranceDetailRequest request) {
        FlightInsurance flightInsurance = flightInsuranceRepository.findById(flightInsuranceId)
                .orElseThrow(NotFoundFlightInsuranceException::new);

        List<SpecialContract> specialContracts = specialContractRepository.findByFlightInsuranceId(flightInsuranceId);

        List<SpecialContractResponse> specialContractResponses = new ArrayList<>();
        for (SpecialContract specialContract : specialContracts) {
            SpecialContractResponse specialContractResponse = SpecialContractResponse.toDTO(specialContract);
            specialContractResponses.add(specialContractResponse);
        }

        return FlightInsuranceDetailResponse.toDTO(flightInsurance, request.getCoverageStartAt(),
                request.getCoverageEndAt(), request.getPremium(), request.getPlanName(), specialContractResponses);
    }

    /**
     * 보험 신청이력 저장
     */
    @Transactional
    public SubscriptionResponse insuranceSubscription(Long userId, SubscriptionRequest request) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        FlightInsurance flightInsurance = flightInsuranceRepository.findById(request.getFlightInsuranceId())
                .orElseThrow(NotFoundFlightInsuranceException::new);

        InsuranceSubscription insuranceSubscription = InsuranceSubscription.createInsuranceSubscription(user, flightInsurance,
                request.getTotalPremium(), request.getCoverageStartAt(), request.getCoverageEndAt(),
                request.getCoverageDetailsTermsContent(), request.getConsentPersonalInfo());

        subscriptionRepository.save(insuranceSubscription);

        return SubscriptionResponse.from(insuranceSubscription.getId());
    }
}
