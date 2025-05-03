package de.hoogvliet.socialservices.socialservice.tsv;

import de.hoogvliet.socialservices.socialservice.CityService;
import de.hoogvliet.socialservices.socialservice.Location;
import de.hoogvliet.socialservices.socialservice.LocationCategoryService;
import de.hoogvliet.socialservices.socialservice.LocationCityService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

@Service  @Log4j2
public class TSVParser {
    private final ResourceLoader resourceLoader;
    @Value("${application.tsv.path}")
    private String TSV_RESOURCE;

    private final LocationMaintenanceService locationMaintenanceService;
    private final TSVCategoryParser categoryParser;
    private final TSVCityParser cityParser;
    private final LocationCategoryService locationCategoryService;
    private final LocationCityService locationCityService;
    private final CityService cityService;

    public TSVParser(LocationMaintenanceService locationMaintenanceService, TSVCategoryParser tsvCategoryParser, TSVCityParser tsvCityParser, LocationCategoryService locationCategoryService, LocationCityService locationCityService, CityService cityService, ResourceLoader resourceLoader) {
        this.locationMaintenanceService = locationMaintenanceService;
        this.categoryParser = tsvCategoryParser;
        this.cityParser = tsvCityParser;
        this.locationCategoryService = locationCategoryService;
        this.locationCityService = locationCityService;
        this.cityService = cityService;
        this.resourceLoader = resourceLoader;
    }

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
        cityService.saveCity(location.getCity());
        locationCategoryService.save(location, categoryParser.getOrCreateCategories(columns));
        locationCityService.save(location, cityParser.getOrCreateCity(columns));
        return location;
    }
}