package com.supportrip.core.airplain.service;

import com.supportrip.core.airplain.domain.AirplainCertification;
import com.supportrip.core.airplain.dto.request.CertificatePnrNumberRequest;
import com.supportrip.core.airplain.repository.AirplainRepository;
import com.supportrip.core.exchange.domain.Country;
import com.supportrip.core.exchange.exception.CountryNotFoundException;
import com.supportrip.core.exchange.repository.CountryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class AirplainServiceTest {

    @Mock
    private AirplainRepository airplainRepository;

    @Mock
    private CountryRepository countryRepository;

    @InjectMocks
    private AirplainService airplainService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCertificatePnrNumber() {
        // Given
        String mockName = "미국";
        String mockUrl = "flag-url";
        String mockCurrency = "USD";
        String mockPnr = "12345";
        LocalDateTime mockDepartAt = LocalDateTime.of(2024, 4, 21, 12, 0);

        CertificatePnrNumberRequest request = new CertificatePnrNumberRequest(mockName, mockPnr, mockDepartAt);
        Country mockCountry = Country.of(mockName, mockUrl, mockCurrency);
        when(countryRepository.findByName(mockName)).thenReturn(mockCountry);

        AirplainCertification mockCertification = AirplainCertification.of(mockCountry,mockPnr,mockDepartAt, true);
        when(airplainRepository.save(any(AirplainCertification.class))).thenReturn(mockCertification);

        // When
        AirplainCertification actual = airplainService.certificatePnrNumber(request);

        // Then
        assertEquals(mockName, actual.getCountry().getName());
        assertEquals(mockPnr, actual.getPnrNumber());
        assertEquals(mockDepartAt, actual.getDepartAt());
        assertTrue(actual.getCertification());
    }

    @Test
    public void testCertificatePnrNumberCountryNotFound() {
        // Given
        CertificatePnrNumberRequest request = new CertificatePnrNumberRequest("지원하지않는국가", "12345", LocalDateTime.of(2024, 4, 21, 12, 0));

        when(countryRepository.findByName("지원하지않는국가")).thenReturn(null);

        // When, Then
        assertThrows(CountryNotFoundException.class, () -> airplainService.certificatePnrNumber(request));
    }
}