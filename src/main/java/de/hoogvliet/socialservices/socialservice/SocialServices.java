package de.hoogvliet.socialservices.socialservice;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import java.util.List;

@Service @Log4j2 @RequiredArgsConstructor
public class SocialServices {
    private final CategoryRepository categoryRepository;
    private final CityRepository cityRepository;
    private final LocationRepository locationRepository;
    private final LocationCategoryRepository locationCategoryRepository;
    private final LocationCityRepository locationCityRepository;

    public List<Location> getAllEntries() {
        return locationRepository.findAll();
    }

    public List<Category> getCategories() {
        return categoryRepository.findAllByOrderByNameAsc();
    }

    public List<City> getCities() {
        return cityRepository.findAll();
    }

    public List<Location> getLocationsByCategory(int categoryId) {
        return locationCategoryRepository.findLocationsByCategoryId(categoryId);
    }

    public List<Location> getLocationsByCity(int cityId) {
        return  locationCityRepository.findLocationsByCityId(cityId);
    }
}