package de.hoogvliet.socialservices.socialservice;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service @Log4j2 @RequiredArgsConstructor
public class SocialServices {
    private final CategoryRepository categoryRepository;
    private final CityRepository cityRepository;
    private final LocationCategoryRepository locationCategoryRepository;

    public List<Category> getCategories() {
        return categoryRepository.findAllByOrderByNameAsc();
    }

    public List<Category> getCategoriesForCity(int cityId) {
        List<Category> categories = locationCategoryRepository.findLocationCategoriesByCity(cityId);
        categories.sort(Comparator.comparing(Category::getName));
        return categories;
    }

    public List<City> getCities() {
        return cityRepository.findAll();
    }

    public List<Location> getLocationsByCategory(int categoryId) {
        return locationCategoryRepository.findLocationsByCategoryId(categoryId);
    }

    public List<Location> getLocationsByCategoryAndCity(Integer categoryId, Integer cityId) {
        return locationCategoryRepository.findLocationsByCategoryIdAndCityId(categoryId, cityId);
    }
}