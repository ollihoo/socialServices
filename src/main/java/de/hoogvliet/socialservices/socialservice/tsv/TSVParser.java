package de.hoogvliet.socialservices.socialservice.tsv;

import de.hoogvliet.socialservices.socialservice.Location;
import de.hoogvliet.socialservices.socialservice.LocationCategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Service  @Log4j2 @RequiredArgsConstructor
public class TSVParser {
    private final ResourceLoader resourceLoader;
    @Value("${application.tsv.pattern}")
    private String TSV_PATTERN;

    private final LocationMaintenanceService locationMaintenanceService;
    private final TSVCategoryParser categoryParser;
    private final LocationCategoryService locationCategoryService;

    public List<Location> getAllEntriesFromTSV() {
        List<Location> locations = new ArrayList<>();
        List<Resource> tsvs = loadTSVResources();
        for (Resource tsv : tsvs) {
            locations.addAll(getEntries(tsv));
        }
        return locations;
    }
            
    private List<Location> getEntries(Resource resource) {
        List<Location> locations = new ArrayList<>();
        String line;
        try (BufferedReader br = openReader(resource)) {
            while ((line = br.readLine()) != null) {
                if (!line.startsWith("Zeitstempel")) {
                    Location location = getOrCreateLocation(line.split("\t"));
                    if (location != null) {
                        locations.add(location);
                    } else {
                        log.warn("Entry deleted: " + line);
                    }
                }
            }
        } catch (IOException e) {
            log.warn("Can't find file that contains data ({}).", TSV_PATTERN);
        }
        return locations;
    }

    private List<Resource> loadTSVResources() {
        log.info("Load TSV files with: " + TSV_PATTERN);
        List<Resource> files = new ArrayList<>(); 
        try {
            Resource[] tsvFiles = ResourcePatternUtils.getResourcePatternResolver(resourceLoader).getResources(TSV_PATTERN);
            log.info(String.format("Found %2d TSV files", tsvFiles.length));
            for (Resource file : tsvFiles) {
                if (isValidFile(file)) {
                    files.add(file);
                } else {
                    log.warn("Ignore TSV file: " + file.getFilename());
                }
            }
        } catch (IOException e) {
            log.warn("TSV parsing failed");
        }
        return files;
    }

    private Boolean isValidFile(Resource resource) {
        String name = resource.getFilename();
        if (name == null) { return false; }
        if (name.contains("TEMPLATE")) { return false; }
        return true;
    }

    private BufferedReader openReader(Resource resource) throws IOException {
        InputStreamReader reader = new InputStreamReader(resource.getInputStream());
        return new BufferedReader(reader);
    }

    private Location getOrCreateLocation(String[] columns) {
        Location location = locationMaintenanceService.createOrUpdateLocation(columns);
        if (location != null) {
            locationCategoryService.save(location, categoryParser.getOrCreateCategories(columns));
            return location;
        }
        return null;
    }
}