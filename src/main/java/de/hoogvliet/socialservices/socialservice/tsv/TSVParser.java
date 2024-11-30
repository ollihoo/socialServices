package de.hoogvliet.socialservices.socialservice.tsv;

import de.hoogvliet.socialservices.socialservice.Location;
import de.hoogvliet.socialservices.socialservice.LocationCategoryService;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

@Service
public class TSVParser {
    public static final String TSV_RESOURCE = "Beratungsstellen.tsv";
    private final LocationMaintenanceService locationMaintenanceService;
    private final TSVCategoryParser categoryParser;
    private final LocationCategoryService locationCategoryService;

    public TSVParser(LocationMaintenanceService locationMaintenanceService, TSVCategoryParser tsvCategoryParser, LocationCategoryService locationCategoryService) {
        this.locationMaintenanceService = locationMaintenanceService;
        this.categoryParser = tsvCategoryParser;
        this.locationCategoryService = locationCategoryService;
    }

    public List<Location> getAllEntriesFromTSV() {
        List<Location> locations = new ArrayList<>();
        String line;
        try {
            BufferedReader br = openReader();
            while ((line = br.readLine()) != null) {
                if (! line.startsWith("Zeitstempel")) {
                    locations.add(getOrCreateLocation(line.split("\t")));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return locations;
    }

    private static BufferedReader openReader() throws IOException {
        ClassPathResource resource = new ClassPathResource(TSV_RESOURCE);
        InputStreamReader reader = new InputStreamReader(resource.getInputStream());
        return new BufferedReader(reader);
    }

    private Location getOrCreateLocation(String[] columns) {
        Location location = locationMaintenanceService.getOrCreateLocation(columns);
        locationCategoryService.save(location, categoryParser.getOrCreateCategories(columns));
        return location;
    }
}
