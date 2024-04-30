package com.supportrip.core.airplane.service;

import com.supportrip.core.airplane.domain.AirplaneCertification;
import com.supportrip.core.airplane.dto.request.CertificatePnrNumberRequest;
import com.supportrip.core.airplane.dto.response.CertificatePnrNumberResponse;
import com.supportrip.core.airplane.repository.AirplaneRepository;
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
public class AirplaneService {
    private static final Random RANDOM = new Random();
    private final AirplaneRepository airplaneRepository;
    private final CountryRepository countryRepository;

    public CertificatePnrNumberResponse certificatePnrNumber(CertificatePnrNumberRequest request) {
        List<Country> countries = countryRepository.findByNameNot("대한민국");
        Country randomCountry = getRandomCountry(countries);

        LocalDateTime randomDepartAt = generateRandomDate(LocalDate.now(), 7, 30);

        return CertificatePnrNumberResponse.of(randomCountry, randomDepartAt);
    }

    public AirplaneCertification createAirplaneCertification(Country country, String pnrNumber, LocalDateTime departAt) {
        return airplaneRepository.save(AirplaneCertification.of(
                country,
                pnrNumber,
                departAt,
                true
        ));
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
