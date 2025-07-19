package de.hoogvliet.socialservices.socialservice;

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
    public void forAGivenLocationEveryCategoryCombinationIsChecked() {
        when(locationCategoryRepository.findByLocationIdAndCategoryId(anyLong(), anyInt())).
                thenReturn(
                        Optional.of(createLocationCategory(GIVEN_LOCATION, GIVEN_CATEGORY, null)));
        locationCategoryService.doCrudOperation(GIVEN_LOCATION, GIVEN_CATEGORIES);
        verify(locationCategoryRepository, times(1)).findByLocationIdAndCategoryId(LOCATION_ID, CATEGORY_ID);
    }

    @Test
    public void forAGivenLocationTheCityNameIsSaved() {
        when(locationCategoryRepository.findByLocationIdAndCategoryId(anyLong(), anyInt())).
                thenReturn(
                        Optional.of(createLocationCategory(GIVEN_LOCATION, GIVEN_CATEGORY, null)));
        locationCategoryService.doCrudOperation(GIVEN_LOCATION, GIVEN_CATEGORIES);
        verify(cityService).saveCity(ANY_CITY_NAME);
    }

    @Test
    public void ifLocationCategoryCombinationDoesNotExistItIsSaved() {
        when(locationCategoryRepository.findByLocationIdAndCategoryId(anyLong(), anyInt())).
                thenReturn(Optional.empty());
        locationCategoryService.doCrudOperation(GIVEN_LOCATION, GIVEN_CATEGORIES);
        verify(locationCategoryRepository).save(any(LocationCategory.class));
    }

    @Test
    public void whenCityIsNotSetInLCItWillBeUpdated() {
        when(locationCategoryRepository.findByLocationIdAndCategoryId(anyLong(), anyInt())).
                thenReturn(
                        Optional.of(createLocationCategory(GIVEN_LOCATION, GIVEN_CATEGORY, null)));
        when(cityService.saveCity(any(String.class))).thenReturn(createCity(34L));

        locationCategoryService.doCrudOperation(GIVEN_LOCATION, GIVEN_CATEGORIES);

        ArgumentCaptor<LocationCategory> captor = ArgumentCaptor.forClass(LocationCategory.class);
        verify(locationCategoryRepository).save(captor.capture());
        assertEquals(34L, captor.getValue().getCity().getId());
    }

    @Test
    public void whenCityDiffersFromLocationItIsSaved() {
        when(locationCategoryRepository.findByLocationIdAndCategoryId(anyLong(), anyInt())).
                thenReturn(Optional.of(
                        createLocationCategory(GIVEN_LOCATION, GIVEN_CATEGORY, createCity(30L))));
        when(cityService.saveCity(any(String.class))).thenReturn(createCity(200L));
        locationCategoryService.doCrudOperation(GIVEN_LOCATION, GIVEN_CATEGORIES);
        ArgumentCaptor<LocationCategory> captor = ArgumentCaptor.forClass(LocationCategory.class);
        verify(locationCategoryRepository).save(captor.capture());
        LocationCategory lc = captor.getValue();
        assertEquals(200L, lc.getCity().getId());
    }

    @Test
    void whenCityIsIdenticalDataWillNotSaved () {
        when(locationCategoryRepository.findByLocationIdAndCategoryId(anyLong(), anyInt())).
                thenReturn(Optional.of(
                        createLocationCategory(GIVEN_LOCATION, GIVEN_CATEGORY, createCity(30L))));
        when(cityService.saveCity(any(String.class))).thenReturn(createCity(30L));
        locationCategoryService.doCrudOperation(GIVEN_LOCATION, GIVEN_CATEGORIES);
        verify(locationCategoryRepository, never()).save(any(LocationCategory.class));
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