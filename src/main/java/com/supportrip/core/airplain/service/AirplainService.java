package com.supportrip.core.airplain.service;

import com.supportrip.core.airplain.domain.AirplainCertification;
import com.supportrip.core.airplain.dto.request.CertificatePnrNumberRequest;
import com.supportrip.core.airplain.repository.AirplainRepository;
import com.supportrip.core.exchange.domain.Country;
import com.supportrip.core.exchange.exception.CountryNotFoundException;
import com.supportrip.core.exchange.repository.CountryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AirplainService {
    private final AirplainRepository airplainRepository;
    private final CountryRepository countryRepository;

    public AirplainCertification certificatePnrNumber(CertificatePnrNumberRequest request) {
        Country country = countryRepository.findByName(request.getCountry());

        if (country == null) {
            throw new CountryNotFoundException();
        }

        // 하나의 티켓에 대해 한번만 거래 가능?

        return airplainRepository.save(
                AirplainCertification.of(
                        country,
                        request.getPnrNumber(),
                        request.getDepartAt(),
                        true
                )
        );
    }
}
