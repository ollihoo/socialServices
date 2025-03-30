package de.hoogvliet.socialservices.socialservice;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


class LocationTest {

    @Test
    public void locationDeliversASimpleListofCategories() {
        Location location = new Location();

        List<LocationCategory> locationCategories = new ArrayList<>();
        locationCategories.add(createLocationCategory(createCategory()));
        location.setLocationCategories(locationCategories);

        List<Category> actualCategories = location.getCategories();

        assertEquals("testcat", actualCategories.getFirst().getName());
    }


    private LocationCategory createLocationCategory(Category category) {
        LocationCategory locationCategory = new LocationCategory();
        locationCategory.setId(2L);
        locationCategory.setCategory(category);
        return locationCategory;
    }

    private Category createCategory() {
        Category category = new Category();
        category.setId(34);
        category.setName("testcat");
        return category;
    }
}