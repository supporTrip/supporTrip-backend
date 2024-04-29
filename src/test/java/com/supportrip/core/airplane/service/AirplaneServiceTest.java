package com.supportrip.core.airplane.service;

import com.supportrip.core.airplane.domain.AirplaneCertification;
import com.supportrip.core.airplane.dto.request.CertificatePnrNumberRequest;
import com.supportrip.core.airplane.repository.AirplaneRepository;
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

class AirplaneServiceTest {

    @Mock
    private AirplaneRepository airplaneRepository;

    @Mock
    private CountryRepository countryRepository;

    @InjectMocks
    private AirplaneService airplaneService;

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

        AirplaneCertification mockCertification = AirplaneCertification.of(mockCountry,mockPnr,mockDepartAt, true);
        when(airplaneRepository.save(any(AirplaneCertification.class))).thenReturn(mockCertification);

        // When
        AirplaneCertification actual = airplaneService.certificatePnrNumber(request);

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
        assertThrows(CountryNotFoundException.class, () -> airplaneService.certificatePnrNumber(request));
    }
}