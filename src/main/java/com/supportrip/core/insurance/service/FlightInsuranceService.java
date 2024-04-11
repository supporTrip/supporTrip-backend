package com.supportrip.core.insurance.service;

import com.supportrip.core.insurance.domain.FlightInsurance;
import com.supportrip.core.insurance.domain.SpecialContract;
import com.supportrip.core.insurance.dto.SearchFlightInsuranceRequestDTO;
import com.supportrip.core.insurance.dto.SearchFlightInsuranceResponseDTO;
import com.supportrip.core.insurance.dto.Top3SpecialContractResponseDTO;
import com.supportrip.core.insurance.repository.FlightInsuranceRepository;
import com.supportrip.core.insurance.repository.SpecialContractRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FlightInsuranceService {
    private final FlightInsuranceRepository flightInsuranceRepository;
    private final FlightInsuranceCalculatePremiumService calculatePremiumService;
    private final SpecialContractRepository specialContractRepository;

    /**
     * 필터링 검색
     */
    public List<SearchFlightInsuranceResponseDTO> findFlightInsuranceFilter(SearchFlightInsuranceRequestDTO requestDTO) {

        int age = calculateAge(requestDTO.getBirthDay());
        int period = calculatePeriod(requestDTO.getDepartAt(), requestDTO.getArrivalAt());

        //연령대와 플랜이 일치하는 보험상품 모두 조회
        List<FlightInsurance> findFlightInsurances = flightInsuranceRepository.findByAgeAndPlan(age, requestDTO.getPlanName());

        //선택된 카테고리가 포함되어있는 보험상품 필터
        List<FlightInsurance> filteredInsurances = flightInsuranceFilter(findFlightInsurances,
                requestDTO.getFlightDelay(),
                requestDTO.getPassportLoss(), requestDTO.getFoodPoisoning());

        //보험료 게산
        List<FlightInsurance> flightInsurances = calculatePremiumService.calculatePremium(age, period, requestDTO.getGender(), filteredInsurances);

        //특약 상위3개 추가
        return addTop3SpecialContract(flightInsurances, requestDTO.getPlanName());
    }

    /**
     * 여행자 보험 상품에 특약 상위3개 리스트로 추가
     */
    private List<SearchFlightInsuranceResponseDTO> addTop3SpecialContract(List<FlightInsurance> flightInsurances, String planName) {
        List<SearchFlightInsuranceResponseDTO> searchFlightInsuranceResponseDTOS = new ArrayList<>();

        for (FlightInsurance flightInsurance : flightInsurances) {
            List<SpecialContract> findSpecialContracts = specialContractRepository.findByFlightInsuranceId(flightInsurance.getId(), PageRequest.of(0, 3));
            List<Top3SpecialContractResponseDTO> contractTop3ResponseDTOS = new ArrayList<>();
            for (SpecialContract findSpecialContract : findSpecialContracts) {
                Top3SpecialContractResponseDTO top3SpecialContractResponseDTO;
                if (planName.equals("standard")) {
                    top3SpecialContractResponseDTO = Top3SpecialContractResponseDTO.standardDTO(findSpecialContract);
                } else {
                    top3SpecialContractResponseDTO = Top3SpecialContractResponseDTO.advancedDTO(findSpecialContract);
                }
                contractTop3ResponseDTOS.add(top3SpecialContractResponseDTO);
            }
            SearchFlightInsuranceResponseDTO searchFlightInsuranceResponseDTO = SearchFlightInsuranceResponseDTO.toDTO(flightInsurance, contractTop3ResponseDTOS);
            searchFlightInsuranceResponseDTOS.add(searchFlightInsuranceResponseDTO);
        }

        return searchFlightInsuranceResponseDTOS;
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
}
