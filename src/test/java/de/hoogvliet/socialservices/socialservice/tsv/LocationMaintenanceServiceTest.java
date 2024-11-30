package de.hoogvliet.socialservices.socialservice.tsv;

import de.hoogvliet.socialservices.socialservice.Location;
import de.hoogvliet.socialservices.socialservice.LocationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LocationMaintenanceServiceTest {
    private final String[] ANY_LOCATION_INPUT = { "TIMESTAMP", "NAME", "SOMEADDRESS", "POSTCODE", "CITY", "https://localhost/" };
    private final String CORRECT_TABLEREFERENCE = "80c1f0329fb983673bddb061a0068098fb372bb05009f9d20077149a74f71634";
    private final Location ANY_LOCATION_FROM_DB = new Location();

    @InjectMocks
    private LocationMaintenanceService locationMaintenanceService;

    @Mock
    private LocationRepository locationRepository;

    @Test public void createLocationParsesInputCorrectly() {
        Location createdLocation = locationMaintenanceService.createLocation(ANY_LOCATION_INPUT);
        assertEquals(ANY_LOCATION_INPUT[1], createdLocation.getName());
        assertEquals(ANY_LOCATION_INPUT[2], createdLocation.getAddress());
        assertEquals(ANY_LOCATION_INPUT[3], createdLocation.getPostCode());
        assertEquals(ANY_LOCATION_INPUT[4], createdLocation.getCity());
        assertEquals(ANY_LOCATION_INPUT[5], createdLocation.getWebsite().toString());
        assertEquals(CORRECT_TABLEREFERENCE, createdLocation.getTableReference());
    }

    @Test public void createLocationDoesntSavesItsInputToDatabase() {
        Location savedLocation = locationMaintenanceService.createLocation(ANY_LOCATION_INPUT);
        verify(locationRepository, never()).save(savedLocation);
    }

    @Test public void getOrCreateLocationCreatesATableReference() {
        Location location = locationMaintenanceService.getOrCreateLocation(ANY_LOCATION_INPUT);
        assertEquals(CORRECT_TABLEREFERENCE, location.getTableReference());
    }

    @Test public void getOrCreateLocationUsesTableReferenceForDatabase() {
        locationMaintenanceService.getOrCreateLocation(ANY_LOCATION_INPUT);
        verify(locationRepository).findByTableReference(CORRECT_TABLEREFERENCE);
    }

    @Test public void getOrCreateLocationReturnsEntryFromDatabase() {
        Optional<Location> locationOptional = Optional.of(ANY_LOCATION_FROM_DB);
        when(locationRepository.findByTableReference(any())).thenReturn(locationOptional);
        Location actualLocation = locationMaintenanceService.getOrCreateLocation(ANY_LOCATION_INPUT);
        assertEquals(ANY_LOCATION_FROM_DB, actualLocation);
    }

    @Test public void getOrCreateLocationReturnsNewEntrySavedInDatabase() {
        Optional<Location> emptyOptional = Optional.empty();
        when(locationRepository.findByTableReference(any())).thenReturn(emptyOptional);
        locationMaintenanceService.getOrCreateLocation(ANY_LOCATION_INPUT);
        verify(locationRepository).findByTableReference(any());
        verify(locationRepository).save(any(Location.class));
    }

    @Test public void createLocationCanHandleEntriesWithoutWebsiteColumn() {
        String[] withInvalidWebsite = { "", "", "", "", "" };
        Location createdLocation = locationMaintenanceService.createLocation(withInvalidWebsite);
        assertNull(createdLocation.getWebsite());
    }

    @Test public void createLocationSkipsInvalidUrls() {
        String[] withInvalidWebsite = { "", "", "", "", "", "invalid url" };
        Location createdLocation = locationMaintenanceService.createLocation(withInvalidWebsite);
        assertNull(createdLocation.getWebsite());
    }
}