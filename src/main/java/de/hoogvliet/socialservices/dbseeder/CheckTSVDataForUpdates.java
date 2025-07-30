package de.hoogvliet.socialservices.dbseeder;

import de.hoogvliet.socialservices.socialservice.CityService;
import de.hoogvliet.socialservices.socialservice.Location;
import de.hoogvliet.socialservices.socialservice.LocationCategoryService;
import de.hoogvliet.socialservices.socialservice.tsv.TSVParser;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.util.List;

@Component @Log4j2 @RequiredArgsConstructor
public class CheckTSVDataForUpdates {
    private final TSVParser tsvParser;
    private final LocationCategoryService locationCategoryService;
    private final CityService cityService;

    @PostConstruct
    public void parseTsvFileAndDoUpdates() {
        List<Location> locations = tsvParser.getAllEntriesFromTSV();
        log.info("Found and checked {} locations.", locations.size());
        locationCategoryService.updateEntriesWithoutCityId();
        locationCategoryService.deleteOrphanedEntries();
        cityService.deleteOrphanedCities();
    }


}
