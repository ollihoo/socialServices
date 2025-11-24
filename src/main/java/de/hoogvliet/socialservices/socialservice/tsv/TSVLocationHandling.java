package de.hoogvliet.socialservices.socialservice.tsv;

import de.hoogvliet.socialservices.socialservice.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j @AllArgsConstructor @Component
public class TSVLocationHandling {
    private final LocationMaintenanceService locationMaintenanceService;
    private final TSVCategoryParser tsvCategoryParser;
    private final LocationCategoryService locationCategoryService;
    private final CityService cityService;

    public Location getOrCreateLocation(String[] tsvLocationEntry) {
        Location location = locationMaintenanceService.createOrUpdateLocation(tsvLocationEntry);
        if (location != null) {
            List<Category> categories = tsvCategoryParser.getOrCreateCategories(tsvLocationEntry);
            City city = cityService.saveCity(location.getCity());
            locationCategoryService.addOrUpdateCategories(location, categories, city);
            locationCategoryService.removeOutdatedCategoriesForLocation(location, categories);
            return location;
        }
        return null;
    }
}
