package de.hoogvliet.socialservices.socialservice;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service @Slf4j
@RequiredArgsConstructor
public class LocationCategoryService {
    private final LocationCategoryRepository locationCategoryRepository;
    private final CityService cityService;

    public void doCrudOperation(Location location, List<Category> categories) {
        City city = cityService.saveCity(location.getCity());
        addOrUpdateCategories(location, categories, city);
        removeOutdatedCategoriesForLocation(location, categories);
    }

    private void removeOutdatedCategoriesForLocation(Location location, List<Category> categories) {
        List<Category> currentCategories = locationCategoryRepository.findCategoriesByLocationId(location.getId());
        currentCategories.forEach(category -> {
            if (! categories.contains(category)) {
                log.info("Deleting category {} {} for location {}", category.getId(), category.getName(), location.getName());
                locationCategoryRepository.deleteByCategoryIdAndLocationId(category.getId(), location.getId());
            }
        });
    }

    private void addOrUpdateCategories(Location location, List<Category> categories, City city) {
        categories.forEach(category -> {
            Optional<LocationCategory> optionalLocationCategory =
                    locationCategoryRepository.findByLocationIdAndCategoryId(location.getId(), category.getId());
            if (optionalLocationCategory.isEmpty()) {
                createLocationCategory(location, category, city);
                return;
            }
            updateCityEntry(optionalLocationCategory.get(), city);
        });
    }

    public void updateCityEntries() {
        locationCategoryRepository.findByCityId(null).forEach(locationCategory -> {
            String cityName = locationCategory.getLocation().getCity();
            City city = cityService.saveCity(cityName);
            locationCategory.setCity(city);
            locationCategoryRepository.save(locationCategory);
        });
    }

    public void deleteOrphanedEntries() {
        locationCategoryRepository.deleteOrphanedLocationMappings();
    }

    private void updateCityEntry(LocationCategory locationCategory, City city) {
        if ((city == null) || ((locationCategory.getCity() != null) &&
            locationCategory.getCity().getId() == city.getId())) {
            return;
        }
        locationCategory.setCity(city);
        locationCategoryRepository.save(locationCategory);
    }

    private void createLocationCategory(Location location, Category category, City city) {
        LocationCategory lc = new LocationCategory();
        lc.setCategory(category);
        lc.setLocation(location);
        lc.setCity(city);
        locationCategoryRepository.save(lc);
    }
}
