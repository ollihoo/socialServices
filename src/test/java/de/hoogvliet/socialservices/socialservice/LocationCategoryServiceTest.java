package de.hoogvliet.socialservices.socialservice;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LocationCategoryServiceTest {
    private static final String ANY_CITY_NAME = "Rotterdam";
    private static final long ANY_CITY_ID = 59L;
    private final long LOCATION_ID = 2L;
    private final int CATEGORY_ID = 25;
    private final String CATEGORY_NAME = "Beratung";

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
    void updateEntriesWithoutCityId_saves_cities() {
        List<LocationCategory> lcList = List.of(createLocationCategory());
        when(locationCategoryRepository.findByCityId(eq(null))).thenReturn(lcList);

        locationCategoryService.updateEntriesWithoutCityId();

        verify(cityService).saveCity(ANY_CITY_NAME);
    }

    @Test
    void updateEntriesWithoutCityId_saves_city_in_locationCategories() {
        City expectedCity = createCity();
        List<LocationCategory> lcList = List.of(createLocationCategory());

        when(locationCategoryRepository.findByCityId(eq(null))).thenReturn(lcList);
        when(cityService.saveCity(anyString())).thenReturn(expectedCity);
        locationCategoryService.updateEntriesWithoutCityId();

        ArgumentCaptor<LocationCategory> captor = ArgumentCaptor.forClass(LocationCategory.class);
        verify(locationCategoryRepository).save(captor.capture());
        assertEquals(expectedCity, captor.getValue().getCity());
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
    @Test
    void addOrUpdateCategories_checks_against_database () {
        City city = createCity();
        Location location = createLocation();
        List<Category> categories = List.of(createCategory(CATEGORY_ID, CATEGORY_NAME));

        when(locationCategoryRepository.findByLocationIdAndCategoryId(anyLong(), anyInt()))
                .thenReturn(Optional.of(createLocationCategory()));

        locationCategoryService.addOrUpdateCategories(location, categories, city);
        verify(locationCategoryRepository).findByLocationIdAndCategoryId(LOCATION_ID, CATEGORY_ID);
    }

    @Test
    void addOrUpdateCategories_sets_missing_city_entry () {
        LocationCategory locationCategoryInDb = createLocationCategory(null);
        City city = createCity();
        Location location = createLocation();
        List<Category> categories = List.of(createCategory(CATEGORY_ID, CATEGORY_NAME));

        when(locationCategoryRepository.findByLocationIdAndCategoryId(anyLong(), anyInt()))
                .thenReturn(Optional.of(locationCategoryInDb));

        locationCategoryService.addOrUpdateCategories(location, categories, city);

        ArgumentCaptor<LocationCategory> captor = ArgumentCaptor.forClass(LocationCategory.class);
        verify(locationCategoryRepository).save(captor.capture());
        assertEquals(ANY_CITY_ID, captor.getValue().getCity().getId());
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
        city.setId(ANY_CITY_ID);
        city.setName(ANY_CITY_NAME);
        return city;
    }

    private LocationCategory createLocationCategory() {
        return createLocationCategory(createCity());
    }

    private LocationCategory createLocationCategory(City city) {
        LocationCategory locationCategory = new LocationCategory();
        locationCategory.setLocation(createLocation());
        locationCategory.setCategory(createCategory(CATEGORY_ID, CATEGORY_NAME));
        locationCategory.setCity(city);
        return locationCategory;

    }

}