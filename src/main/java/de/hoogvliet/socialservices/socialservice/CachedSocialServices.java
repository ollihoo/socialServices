package de.hoogvliet.socialservices.socialservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CachedSocialServices {
    @Autowired private CategoryRepository categoryRepository;
    @Autowired private LocationRepository locationRepository;


    public List<Location> getAllEntries() {
        return locationRepository.findAll();
    }

    public List<Category> getCategories() {
        return categoryRepository.findAll();
    }
}
