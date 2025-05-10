package de.hoogvliet.socialservices.socialservice;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CityServiceTest {

    public static final String ANY_CITY_NAME = "cityName";
    @InjectMocks
    private CityService cityService;

    @Mock
    private CityRepository cityRepository;

    @Test
    void saveCitySavesNewEntries() {
        when(cityRepository.findByName(anyString())).thenReturn(Optional.empty());
        cityService.saveCity(ANY_CITY_NAME);
        verify(cityRepository).save(any(City.class));
    }

    @Test
    void saveCityChecksForExistingEntry() {
        when(cityRepository.findByName(anyString())).thenReturn(Optional.empty());
        cityService.saveCity(ANY_CITY_NAME);
        verify(cityRepository).findByName(ANY_CITY_NAME);
    }

}