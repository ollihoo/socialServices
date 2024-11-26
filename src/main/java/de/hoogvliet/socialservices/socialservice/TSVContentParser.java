package de.hoogvliet.socialservices.socialservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

@Service
public class TSVContentParser {
    @Autowired private LocationService locationService;
    @Autowired private CategoryService categoryService;
    @Autowired private LocationCategoryService locationCategoryService;

    public List<Location> getAllEntriesFromTSV() {
        ClassPathResource resource = new ClassPathResource("Beratungsstellen.tsv");
        List<Location> locations = new ArrayList<>();
        InputStreamReader reader;
        try {
            String line;
            reader = new InputStreamReader(resource.getInputStream());
            BufferedReader br = new BufferedReader(reader);
            while ((line = br.readLine()) != null) {
                if (! line.startsWith("Zeitstempel")) {
                    String[] columns = line.split("\t");
                    locations.add(getOrCreateLocation(columns));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return locations;
        }

        return locations;
    }

    private Location getOrCreateLocation(String[] columns) {
        Location location = locationService.getOrCreateLocation(columns);
        locationCategoryService.save(location, categoryService.getOrCreateCategories(columns));
        return location;
    }





}
