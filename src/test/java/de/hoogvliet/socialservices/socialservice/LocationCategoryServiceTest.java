package de.hoogvliet.socialservices.socialservice;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) class LocationCategoryServiceTest {
    public static final long LOCATION_ID = 2L;
    public static final int CATEGORY_ID = 25;
    private static List<Category> categories;
    private static Location location;
    public static final LocationCategory VALID_LOCCAT = new LocationCategory();
    @InjectMocks private LocationCategoryService locationCategoryService;

    @Mock
    private LocationCategoryRepository repository;

    @BeforeEach
    public void setup() {
        categories = createCategoryList();
        location = createValidLocation();
    }

    @Test
    public void saveSearchesForAGivenCombination() {
        when(repository.findByLocationIdAndCategoryId(anyLong(), anyInt())).
                thenReturn(createOptionalForLocationCategory());
        locationCategoryService.save(location, categories);
        verify(repository).findByLocationIdAndCategoryId(LOCATION_ID, CATEGORY_ID);
    }

    @Test public void saveSavesCombinationWhenNotThere() {
        when(repository.findByLocationIdAndCategoryId(anyLong(), anyInt())).
                thenReturn(createEmptyOptionalForLocationCategory());
        locationCategoryService.save(location, categories);
        verify(repository).save(any(LocationCategory.class));
    }

    @Test public void saveDoesNotSaveCombinationWhenAvailable() {
        when(repository.findByLocationIdAndCategoryId(anyLong(), anyInt())).
                thenReturn(createOptionalForLocationCategory());
        locationCategoryService.save(location, categories);
        verify(repository, never()).save(any(LocationCategory.class));
    }

    private static List<Category> createCategoryList() {
        List<Category> categories = new ArrayList<>();
        categories.add(createValidCategory());
        return categories;
    }

    private static Optional<LocationCategory> createOptionalForLocationCategory() {
        return Optional.of(VALID_LOCCAT);
    }
    private Optional<LocationCategory> createEmptyOptionalForLocationCategory() {
        return Optional.empty();
    }

    private static Location createValidLocation() {
        Location location = new Location();
        location.setId(LOCATION_ID);
        return location;
    }

    private static Category createValidCategory() {
        Category category = new Category();
        category.setId(CATEGORY_ID);
        return category;
    }
}