package de.hoogvliet.socialservices.socialservice;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

@Service
public class SocialServices {

    private final LocationService locationService;
    private final CategoryService categoryService;

    private final LocationCategoryService locationCategoryService;

    public SocialServices(CategoryService categoryService,
                          LocationService locationService,
                          LocationCategoryService locationCategoryService) {
        this.categoryService = categoryService;
        this.locationService = locationService;
        this.locationCategoryService = locationCategoryService;
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
        return categoryService.getAllCategories();
    }

    private Location getOrCreateLocation(String[] columns) {
        Location location = locationService.getOrCreateLocation(columns);
        locationCategoryService.save(location, categoryService.getOrCreateCategories(columns));
        return location;
    }





}
