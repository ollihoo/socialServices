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

    public void updateEntriesWithoutCityId() {
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

    public void removeOutdatedCategoriesForLocation(Location location, List<Category> correctCategories) {
        List<Category> categoriesInDb = locationCategoryRepository.findCategoriesByLocationId(location.getId());
        categoriesInDb.forEach(categoryInQuestion -> {
            if (! correctCategories.contains(categoryInQuestion)) {
                log.info("Deleting category '({}) {}' for location {}", categoryInQuestion.getId(), categoryInQuestion.getName(), location.getName());
                locationCategoryRepository.deleteByCategoryIdAndLocationId(categoryInQuestion.getId(), location.getId());
            }
        });
    }

    public void addOrUpdateCategories(Location location, List<Category> categories, City city) {
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
