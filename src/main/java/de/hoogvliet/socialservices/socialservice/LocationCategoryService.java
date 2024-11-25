package de.hoogvliet.socialservices.socialservice;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LocationCategoryService {
    private final LocationCategoryRepository locationCategoryRepository;

    public LocationCategoryService(LocationCategoryRepository repository) {
        this.locationCategoryRepository = repository;
    }

    public void save(Location location, List<Category> categories) {
        categories.forEach(category -> {
            Optional<LocationCategory> optionalLocationCategory = locationCategoryRepository.findByLocationIdAndCategoryId(location.getId(), category.getId());
            if (optionalLocationCategory.isEmpty()) {
                createLocationCategory(location, category);
            }
        });
    }

    private void createLocationCategory(Location location, Category category) {
        LocationCategory lc = new LocationCategory();
        lc.setCategory(category);
        lc.setLocation(location);
        locationCategoryRepository.save(lc);
    }
}
