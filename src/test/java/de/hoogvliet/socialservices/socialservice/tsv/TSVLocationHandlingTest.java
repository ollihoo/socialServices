package de.hoogvliet.socialservices.socialservice.tsv;

import de.hoogvliet.socialservices.socialservice.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class TSVLocationHandlingTest {

    private final String[] tsvLocationEntry = { "", "", "", "","","", "" };


    @InjectMocks
    private TSVLocationHandling tsvLocationHandling;


    @Mock private LocationMaintenanceService locationMaintenanceService;
    @Mock private TSVCategoryParser tsvCategoryParser;
    @Mock private CityService cityService;
    @Mock private LocationCategoryService locationCategoryService;

    @Test
    void getOrCreateLocation_checks_location_for_insertion_or_update() {
        when(locationMaintenanceService.createOrUpdateLocation(any())).thenReturn(getValidLocation());
        tsvLocationHandling.getOrCreateLocation(tsvLocationEntry);
        verify(locationMaintenanceService).createOrUpdateLocation(tsvLocationEntry);
    }

    @Test
    void getOrCreateLocation_returns_location() {
        Location location = getValidLocation();
        when(locationMaintenanceService.createOrUpdateLocation(any())).thenReturn(location);
        assertEquals(location, tsvLocationHandling.getOrCreateLocation(tsvLocationEntry));
    }

    @Test
    void getOrCreateLocation_returns_null_for_invalid_location() {
        when(locationMaintenanceService.createOrUpdateLocation(any())).thenReturn(null);
        assertNull(tsvLocationHandling.getOrCreateLocation(tsvLocationEntry));
    }


    @Test
    void getOrCreateLocation_for_location_get_or_create_categories() {
        when(locationMaintenanceService.createOrUpdateLocation(any())).thenReturn(getValidLocation());
        tsvLocationHandling.getOrCreateLocation(tsvLocationEntry);
        verify(tsvCategoryParser).getOrCreateCategories(tsvLocationEntry);
    }

    @Test
    void getOrCreateLocation_for_location_save_associated_city() {
        when(locationMaintenanceService.createOrUpdateLocation(any())).thenReturn(getValidLocation());
        tsvLocationHandling.getOrCreateLocation(tsvLocationEntry);
        verify(cityService).saveCity("Leipzig");
    }

    @Test
    void getOrCreateLocation_for_location_save_associated_categories() {
        City city = new City();
        Location location = getValidLocation();
        List<Category> categories = getValidCategories();
        when(locationMaintenanceService.createOrUpdateLocation(any())).thenReturn(location);
        when(cityService.saveCity(any())).thenReturn(city);
        when(tsvCategoryParser.getOrCreateCategories(tsvLocationEntry)).thenReturn(categories);
        tsvLocationHandling.getOrCreateLocation(tsvLocationEntry);
        verify(locationCategoryService).addOrUpdateCategories(location, categories, city);
    }

    @Test
    void getOrCreateLocation_for_location_remove_outdated_categories() {
        City city = new City();
        Location location = getValidLocation();
        List<Category> categories = getValidCategories();
        when(locationMaintenanceService.createOrUpdateLocation(any())).thenReturn(location);
        when(cityService.saveCity(any())).thenReturn(city);
        when(tsvCategoryParser.getOrCreateCategories(tsvLocationEntry)).thenReturn(categories);
        tsvLocationHandling.getOrCreateLocation(tsvLocationEntry);
        verify(locationCategoryService).removeOutdatedCategoriesForLocation(location, categories);
    }


    private Location getValidLocation() {
        Location location = new Location();
        location.setCity("Leipzig");
        return location;
    }

    private List<Category> getValidCategories() {
        List<Category> categories = new ArrayList<>();
        categories.add(new Category());
        return categories;
    }

}