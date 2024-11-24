package de.hoogvliet.socialservices.socialservice;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

@Service
public class SocialServices {
    private static final int COLUMN_CATEGORIES = 6;

    private final CategoryRepository categoryRepository;
    private final LocationService locationService;
    private final LocationCategoryRepository locationCategoryRepository;

    public SocialServices(CategoryRepository categoryRepository,
                          LocationService locationService,
                          LocationCategoryRepository locationCategoryRepository) {
        this.categoryRepository = categoryRepository;
        this.locationService = locationService;
        this.locationCategoryRepository = locationCategoryRepository;
    }

    public List<Location> getAllEntries() {
        ClassPathResource resource = new ClassPathResource("Beratungsstellen.tsv");
        List<Location> locations = new ArrayList<>();
        InputStreamReader reader;
        try {
            reader = new InputStreamReader(resource.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
            return locations;
        }
        try (BufferedReader br = new BufferedReader(reader)) {
            String line;
            while ((line = br.readLine()) != null) {
                if (! line.startsWith("Zeitstempel")) {
                    String[] columns = line.split("\t");
                    locations.add(getOrCreateLocation(columns));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return locations;
    }


    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }


    private Location getOrCreateLocation(String[] columns) {
        Location location = locationService.getOrCreateLocation(columns);
        saveLocationCategories(location, getOrCreateCategories(columns));
        return location;
    }



    private void saveLocationCategories(Location location, List<Category> categories) {
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


    private List<Category> getOrCreateCategories(String[] entry) {
        List<Category> categorySet = new ArrayList<>();
        if (entry.length >= 7) {
            String[] categories = entry[COLUMN_CATEGORIES].split(", ?");
            Arrays.stream(categories).forEach((String cat) -> {
                Optional<Category> optionalCategory =categoryRepository.findByName(cat);
                if (optionalCategory.isPresent()) {
                    categorySet.add(optionalCategory.get());
                } else {
                    categorySet.add(createCategory(cat));
                }
            });
        }
        return categorySet;
    }

    private Category createCategory(String cat) {
        Category myCat = new Category();
        myCat.setName(cat);
        categoryRepository.save(myCat);
        return myCat;
    }


}
