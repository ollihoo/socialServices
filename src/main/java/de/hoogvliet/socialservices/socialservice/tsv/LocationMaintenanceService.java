package de.hoogvliet.socialservices.socialservice.tsv;

import de.hoogvliet.socialservices.socialservice.Location;
import de.hoogvliet.socialservices.socialservice.LocationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service @Log4j2 @RequiredArgsConstructor
public class LocationMaintenanceService {

    private final LocationRepository locationRepository;
    private final TSVColumnParser tsvColumnParser;

    public Location createOrUpdateLocation(String[] columns) {
        List<String> categories = TSVCategoryParser.splitCategoriesEntry(columns);

        Location locationDb = getLocation(columns);
        return (locationDb == null)?
                (categories.isEmpty())?
                        null:
                        createLocation(columns):
                (categories.isEmpty())?
                        deleteLocation(locationDb):
                        updateLocation(columns, locationDb);
    }

    private Location deleteLocation(Location locationDb) {
        log.warn("Deleting location {} (ID: {})", locationDb.getName(), locationDb.getId());
        locationRepository.delete(locationDb);
        return null;
    }

    public Location getLocation(String[] columns) {
        String tableReference = tsvColumnParser.createTableReference(columns);
        Optional<Location> locationOptional = locationRepository.findByTableReference(tableReference);
        return locationOptional.orElse(null);
    }

    public Location createLocation(String[] columns) {
         return locationRepository.save(
                tsvColumnParser.createLocation(columns)
        );
    }

    public Location updateLocation(String[] columns, Location locationDb) {
        return locationRepository.save(
                tsvColumnParser.updateLocation(columns, locationDb)
        );
    }

}
