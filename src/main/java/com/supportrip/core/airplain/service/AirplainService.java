package com.supportrip.core.airplain.service;

import com.supportrip.core.airplain.domain.AirplainCertification;
import com.supportrip.core.airplain.dto.request.CertificatePnrNumberRequest;
import com.supportrip.core.airplain.repository.AirplainRepository;
import com.supportrip.core.exchange.domain.Country;
import com.supportrip.core.exchange.repository.CountryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class AirplainService {
    private static final Random RANDOM = new Random();
    private final AirplainRepository airplainRepository;
    private final CountryRepository countryRepository;

    public AirplainCertification certificatePnrNumber(CertificatePnrNumberRequest request) {
        List<Country> countries = countryRepository.findAll();
        Country randomCountry = getRandomCountry(countries);

        LocalDateTime randomDepartAt = generateRandomDate(LocalDate.now(), 7, 30);

        return airplainRepository.save(
                AirplainCertification.of(
                        randomCountry,
                        request.getPnrNumber(),
                        randomDepartAt,
                        true
                )
        );
    }

    private Country getRandomCountry(List<Country> countries) {
        int randomIndex = RANDOM.nextInt(countries.size());
        return countries.get(randomIndex);
    }

    private LocalDateTime generateRandomDate(LocalDate baseDate, int minDaysToAdd, int maxDaysToAdd ) {
        int randomDaysToAdd = RANDOM.nextInt(maxDaysToAdd - minDaysToAdd + 1) + minDaysToAdd;
        LocalDate randomDate = baseDate.plusDays(randomDaysToAdd);

        int randomHour = RANDOM.nextInt(24);
        int randomMinute = RANDOM.nextInt(60);
        LocalTime randomTime = LocalTime.of(randomHour, randomMinute, 0);

        return LocalDateTime.of(randomDate, randomTime);
    }

}
