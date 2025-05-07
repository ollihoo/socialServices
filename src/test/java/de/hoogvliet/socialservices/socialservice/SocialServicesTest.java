package de.hoogvliet.socialservices.socialservice;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SocialServicesTest {
    private static final List<Category> ANY_CATEGORY_LIST = Collections.emptyList();
    private final List<Location> ANY_LOCATION_LIST = Collections.emptyList();

    @InjectMocks
    private SocialServices socialServices;

    @Mock
    private LocationCategoryRepository locationCategoryRepository;

    @Test
    void getLocationsByCategoriesReturnsCorrectListOfLocations() {
        when(locationCategoryRepository.findLocationsByCategoryId(anyInt())).thenReturn(ANY_LOCATION_LIST);
        List<Location> locationsByCategory = socialServices.getLocationsByCategory(1);
        assertEquals(locationsByCategory, ANY_LOCATION_LIST);
    }

    @Test
    void getLocationsByCategoriesUsesCorrectRepository() {
        when(locationCategoryRepository.findLocationsByCategoryId(anyInt())).thenReturn(ANY_LOCATION_LIST);
        socialServices.getLocationsByCategory(1);
        verify(locationCategoryRepository).findLocationsByCategoryId(1);
    }

    @Test
    void getCategoriesByCityIdReturnsACorrectList() {
        List<Category> actualList = socialServices.getCategoriesForCity(23);
        assertEquals(ANY_CATEGORY_LIST, actualList);
    }

    @Test
    void getCategoriesByCityUsesGetLocationCategoriesForCity() {
        when(locationCategoryRepository.findLocationCategoriesByCity(anyInt())).thenReturn(ANY_CATEGORY_LIST);
        socialServices.getCategoriesForCity(23);
        verify(locationCategoryRepository).findLocationCategoriesByCity(23);
    }

}