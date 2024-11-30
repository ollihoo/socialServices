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

    @Test public void createOrUpdateLocationCreatesATableReference() {
        Location location = locationMaintenanceService.createOrUpdateLocation(ANY_LOCATION_INPUT);
        assertEquals(CORRECT_TABLEREFERENCE, location.getTableReference());
    }

    @Test public void createOrUpdateLocationUsesTableReferenceForDatabase() {
        locationMaintenanceService.createOrUpdateLocation(ANY_LOCATION_INPUT);
        verify(locationRepository).findByTableReference(CORRECT_TABLEREFERENCE);
    }

    @Test public void createOrUpdateLocationReturnsEntryFromDatabase() {
        Optional<Location> locationOptional = Optional.of(ANY_LOCATION_FROM_DB);
        when(locationRepository.findByTableReference(any())).thenReturn(locationOptional);
        Location actualLocation = locationMaintenanceService.createOrUpdateLocation(ANY_LOCATION_INPUT);
        assertEquals(ANY_LOCATION_FROM_DB, actualLocation);
    }

    @Test public void createOrUpdateLocationReturnsNewEntrySavedInDatabase() {
        Optional<Location> emptyOptional = Optional.empty();
        when(locationRepository.findByTableReference(any())).thenReturn(emptyOptional);
        locationMaintenanceService.createOrUpdateLocation(ANY_LOCATION_INPUT);
        verify(locationRepository).findByTableReference(any());
        verify(locationRepository).save(any(Location.class));
    }

    @Test public void createOrUpdateLocationCanHandleEntriesWithoutWebsiteColumn() {
        String[] withInvalidWebsite = { "", "", "", "", "" };
        Location createdLocation = locationMaintenanceService.createOrUpdateLocation(withInvalidWebsite);
        assertNull(createdLocation.getWebsite());
    }

    @Test public void createOrUpdateLocationSkipsInvalidUrls() {
        String[] withInvalidWebsite = { "", "", "", "", "", "invalid url" };
        Location createdLocation = locationMaintenanceService.createOrUpdateLocation(withInvalidWebsite);
        assertNull(createdLocation.getWebsite());
    }
}