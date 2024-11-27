package de.hoogvliet.socialservices.socialservice;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LocationServiceTest {
    private final String[] ANY_LOCATION_INPUT = { "TIMESTAMP", "NAME", "SOMEADDRESS", "POSTCODE", "CITY", "https://localhost/" };
    private final String CORRECT_TABLEREFERENCE = "80c1f0329fb983673bddb061a0068098fb372bb05009f9d20077149a74f71634";
    @InjectMocks
    private LocationService locationService;

    @Mock
    private LocationRepository locationRepository;

    @Test public void createLocationParsesInputCorrectly() {
        Location createdLocation = locationService.createLocation(CORRECT_TABLEREFERENCE, ANY_LOCATION_INPUT);
        assertEquals(ANY_LOCATION_INPUT[1], createdLocation.getName());
        assertEquals(ANY_LOCATION_INPUT[2], createdLocation.getAddress());
        assertEquals(ANY_LOCATION_INPUT[3], createdLocation.getPostCode());
        assertEquals(ANY_LOCATION_INPUT[4], createdLocation.getCity());
        assertEquals(ANY_LOCATION_INPUT[5], createdLocation.getWebsite().toString());
        assertEquals(CORRECT_TABLEREFERENCE, createdLocation.getTableReference());
    }

    @Test public void createLocationSavesItsInputToDatabase() {
        Location savedLocation = locationService.createLocation(CORRECT_TABLEREFERENCE, ANY_LOCATION_INPUT);
        verify(locationRepository).save(savedLocation);
    }

    @Test public void getOrCreateLocationCreatesATableReference() {
        Location location = locationService.getOrCreateLocation(ANY_LOCATION_INPUT);
        assertEquals(CORRECT_TABLEREFERENCE, location.getTableReference());
    }

    @Test public void getOrCreateLocationUsesTableReferenceForDatabase() {
        locationService.getOrCreateLocation(ANY_LOCATION_INPUT);
        verify(locationRepository).findByTableReference(CORRECT_TABLEREFERENCE);
    }

    @Test public void getOrCreateLocationReturnsEntryFromDatabase() {
        Location dbLocation = new Location();
        Optional<Location> locationOptional = Optional.of(dbLocation);
        when(locationRepository.findByTableReference(any())).thenReturn(locationOptional);
        Location actualLocation = locationService.getOrCreateLocation(ANY_LOCATION_INPUT);
        assertEquals(dbLocation, actualLocation);
    }

    @Test public void getOrCreateLocationReturnsNewEntrySavedInDatabase() {
        Optional<Location> emptyOptional = Optional.empty();
        when(locationRepository.findByTableReference(any())).thenReturn(emptyOptional);
        locationService.getOrCreateLocation(ANY_LOCATION_INPUT);
        verify(locationRepository).findByTableReference(any());
        verify(locationRepository).save(any(Location.class));
    }

}