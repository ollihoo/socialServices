package de.hoogvliet.socialservices.socialservice.tsv;

import de.hoogvliet.socialservices.socialservice.Location;
import de.hoogvliet.socialservices.socialservice.LocationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LocationMaintenanceServiceTest {
    private final String[] ANY_LOCATION_INPUT = { "TIMESTAMP", "NAME", "SOME ADDRESS", "POSTCODE", "CITY", "https://localhost/" };
    private final String CORRECT_TABLE_REFERENCE = "80c1f0329fb983673bddb061a0068098fb372bb05009f9d20077149a74f71634";
    private final Location ANY_LOCATION_FROM_DB = new Location();
    private final Location ANY_LOCATION = new Location();
    private final Location ANY_LOCATION_UPDATED = new Location();

    @InjectMocks
    private LocationMaintenanceService locationMaintenanceService;

    @Mock
    private LocationRepository locationRepository;
    @Mock
    private TSVColumnParser tsvColumnParser;

    @Test
    void getLocation_uses_table_reference_for_requests() {
        when(tsvColumnParser.createTableReference(any())).thenReturn(CORRECT_TABLE_REFERENCE);
        when(locationRepository.findByTableReference(anyString()))
                .thenReturn(Optional.of(ANY_LOCATION_FROM_DB));
        locationMaintenanceService.getLocation(ANY_LOCATION_INPUT);
        verify(locationRepository).findByTableReference(CORRECT_TABLE_REFERENCE);
    }

    @Test
    void getLocation_returns_existing_location_from_db() {
        when(tsvColumnParser.createTableReference(any())).thenReturn(CORRECT_TABLE_REFERENCE);
        when(locationRepository.findByTableReference(anyString()))
                .thenReturn(Optional.of(ANY_LOCATION_FROM_DB));
        assertEquals(
                ANY_LOCATION_FROM_DB,
                locationMaintenanceService.getLocation(ANY_LOCATION_INPUT));
    }

    @Test
    void getLocation_returns_null_if_nothing_was_found() {
        when(tsvColumnParser.createTableReference(any())).thenReturn(CORRECT_TABLE_REFERENCE);
        when(locationRepository.findByTableReference(anyString()))
                .thenReturn(Optional.empty());
        assertNull(locationMaintenanceService.getLocation(ANY_LOCATION_INPUT));
    }

    @Test
    void createLocation_create_new_entity() {
        locationMaintenanceService.createLocation(ANY_LOCATION_INPUT);
        verify(tsvColumnParser).createLocation(ANY_LOCATION_INPUT);
    }

    @Test
    void createLocation_returns_saved_entity() {
        when(tsvColumnParser.createLocation(any())).thenReturn(ANY_LOCATION);
        when(locationRepository.save(any())).thenReturn(ANY_LOCATION_FROM_DB);

        Location actualLocation = locationMaintenanceService.createLocation(ANY_LOCATION_INPUT);

        assertEquals(ANY_LOCATION_FROM_DB,actualLocation);
    }

    @Test
    void updateLocation_updates_entity() {
        locationMaintenanceService.updateLocation(ANY_LOCATION_INPUT, ANY_LOCATION_FROM_DB);
        verify(tsvColumnParser).updateLocation(ANY_LOCATION_INPUT, ANY_LOCATION_FROM_DB);
    }

    @Test
    void updateLocation_returns_saved_entity() {
        when(tsvColumnParser.updateLocation(any(), any())).thenReturn(ANY_LOCATION);
        when(locationRepository.save(any())).thenReturn(ANY_LOCATION_UPDATED);

        Location actualLocation = locationMaintenanceService.updateLocation(ANY_LOCATION_INPUT, ANY_LOCATION_FROM_DB);

        assertEquals(ANY_LOCATION_UPDATED,actualLocation);
    }

}