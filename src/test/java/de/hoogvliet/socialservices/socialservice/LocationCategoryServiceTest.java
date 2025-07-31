package de.hoogvliet.socialservices.socialservice;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LocationCategoryServiceTest {
    private final long LOCATION_ID = 2L;
    private final int CATEGORY_ID = 25;
    private final String CATEGORY_NAME = "Beratung";

    private static final String ANY_CITY_NAME = "Rotterdam";

    @Mock
    private CityService cityService;
    @Mock
    private LocationCategoryRepository locationCategoryRepository;
    @InjectMocks
    private LocationCategoryService locationCategoryService;

    @Test
    void deleteOrphanedEntries_removes_all_locations_without_categories() {
        locationCategoryService.deleteOrphanedEntries();
        verify(locationCategoryRepository, times(1)).deleteOrphanedLocationMappings();
    }

    @Test
    void updateEntriesWithoutCityId_search_for_entries_with_cityId_null() {
        List<LocationCategory> lcList = List.of(createLocationCategory());
        when(locationCategoryRepository.findByCityId(eq(null))).thenReturn(lcList);

        locationCategoryService.updateEntriesWithoutCityId();

        verify(locationCategoryRepository).findByCityId(null);
    }

    @Test
    void removeOutdatedCategories_same_entry_in_Db_nothing_is_deleted() {
        List<Category> dbCategories = List.of(createCategory(CATEGORY_ID, CATEGORY_NAME));
        List<Category> recentCategories = List.of(createCategory(CATEGORY_ID, CATEGORY_NAME));
        when(locationCategoryRepository.findCategoriesByLocationId(anyLong())).thenReturn(dbCategories);

        locationCategoryService.removeOutdatedCategoriesForLocation(createLocation(), recentCategories);
        verify(locationCategoryRepository, never()).deleteByCategoryIdAndLocationId(CATEGORY_ID, LOCATION_ID);
    }

    @Test
    void removeOutdatedCategories_outdated_entry_is_deleted() {
        Location location = createLocation();
        List<Category> dbCategories = List.of(createCategory(CATEGORY_ID, CATEGORY_NAME));
        List<Category> recentCategories = List.of(createCategory(26, "another category"));
        when(locationCategoryRepository.findCategoriesByLocationId(anyLong())).thenReturn(dbCategories);

        locationCategoryService.removeOutdatedCategoriesForLocation(location, recentCategories);
        verify(locationCategoryRepository).deleteByCategoryIdAndLocationId(CATEGORY_ID, LOCATION_ID);
    }


    private Location createLocation() {
        Location location = new Location();
        location.setId(LOCATION_ID);
        location.setCity(ANY_CITY_NAME);
        return location;
    }

    private Category createCategory(int categoryId, String categoryName) {
        Category category = new Category();
        category.setId(categoryId);
        category.setName(categoryName);
        return category;
    }

    private City createCity() {
        City city = new City();
        city.setId(59L);
        city.setName(ANY_CITY_NAME);
        return city;
    }

    private LocationCategory createLocationCategory() {
        LocationCategory locationCategory = new LocationCategory();
        locationCategory.setLocation(createLocation());
        locationCategory.setCategory(createCategory(CATEGORY_ID, CATEGORY_NAME));
        locationCategory.setCity(createCity());
        return locationCategory;
    }

}