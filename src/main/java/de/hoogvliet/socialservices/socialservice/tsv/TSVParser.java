package de.hoogvliet.socialservices.socialservice.tsv;

import de.hoogvliet.socialservices.socialservice.Location;
import de.hoogvliet.socialservices.socialservice.LocationCategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Service  @Log4j2 @RequiredArgsConstructor
public class TSVParser {
    private final ResourceLoader resourceLoader;
    @Value("${application.tsv.path}")
    private String TSV_RESOURCE;

    private final LocationMaintenanceService locationMaintenanceService;
    private final TSVCategoryParser categoryParser;
    private final LocationCategoryService locationCategoryService;

    public List<Location> getAllEntriesFromTSV() {
        List<Location> locations = new ArrayList<>();
        String line;

        try (BufferedReader br = openReader()) {
            while ((line = br.readLine()) != null) {
                if (!line.startsWith("Zeitstempel")) {
                    locations.add(getOrCreateLocation(line.split("\t")));
                }
            }
        } catch (IOException e) {
            log.warn("Can't find file that contains data ({}).", TSV_RESOURCE);
        }
        return locations;
    }

    private BufferedReader openReader() throws IOException {
        Resource resource = resourceLoader.getResource(TSV_RESOURCE);
        InputStreamReader reader = new InputStreamReader(resource.getInputStream());
        return new BufferedReader(reader);
    }

    private Location getOrCreateLocation(String[] columns) {
        Location location = locationMaintenanceService.createOrUpdateLocation(columns);
        locationCategoryService.save(location, categoryParser.getOrCreateCategories(columns));
        return location;
    }
}
