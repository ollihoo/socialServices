package de.hoogvliet.socialservices.socialservice;

import de.hoogvliet.socialservices.socialservice.tsv.TSVLocationHandling;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LocationCategoryServiceTest {
    private final long LOCATION_ID = 2L;
    private final int CATEGORY_ID = 25;

    private static final String ANY_CITY_NAME = "Rotterdam";

    private List<Category> GIVEN_CATEGORIES;
    private Location GIVEN_LOCATION;
    private Category GIVEN_CATEGORY;

    @Mock
    private CityService cityService;
    @Mock
    private LocationCategoryRepository locationCategoryRepository;
    @InjectMocks
    private LocationCategoryService locationCategoryService;

    @BeforeEach
    public void setup() {
        GIVEN_LOCATION = createLocation();
        GIVEN_CATEGORY = createCategory();
        GIVEN_CATEGORIES = createCategoryList();
    }

    @Test
    void deleteOrphanedEntries_removes_all_locations_without_categories() {
        locationCategoryService.deleteOrphanedEntries();
        verify(locationCategoryRepository, times(1)).deleteOrphanedLocationMappings();
    }

    private List<Category> createCategoryList() {
        List<Category> categories = new ArrayList<>();
        categories.add(GIVEN_CATEGORY);
        return categories;
    }

    private Location createLocation() {
        Location location = new Location();
        location.setId(LOCATION_ID);
        location.setCity(ANY_CITY_NAME);
        return location;
    }

    private Category createCategory() {
        Category category = new Category();
        category.setId(CATEGORY_ID);
        return category;
    }

    private static City createCity(long cityId) {
        City VALID_CITY = new City();
        VALID_CITY.setId(cityId);
        return VALID_CITY;
    }

    private LocationCategory createLocationCategory(Location location, Category category, City city) {
        LocationCategory locationCategory = new LocationCategory();
        locationCategory.setLocation(location);
        locationCategory.setCategory(category);
        locationCategory.setCity(city);
        return locationCategory;
    }

}