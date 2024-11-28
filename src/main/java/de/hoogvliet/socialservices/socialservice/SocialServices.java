package de.hoogvliet.socialservices.socialservice;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import java.util.List;

@Service @Log4j2
public class SocialServices {
    private final CategoryRepository categoryRepository;
    private final LocationRepository locationRepository;
    private final LocationCategoryRepository locationCategoryRepository;
    public SocialServices(CategoryRepository categoryRepository,
                          LocationRepository locationRepository,
                          LocationCategoryRepository locationCategoryRepository) {
        this.categoryRepository = categoryRepository;
        this.locationRepository = locationRepository;
        this.locationCategoryRepository = locationCategoryRepository;
    }

    public List<Location> getAllEntries() {
        return locationRepository.findAll();
    }

    public List<Category> getCategories() {
        return categoryRepository.findAll();
    }

    public List<Location> getLocationsByCategory(int categoryId) {
        return locationCategoryRepository.findLocationsByCategoryId(categoryId);
    }
}
