package de.hoogvliet.socialservices.socialservice.tsv;

import de.hoogvliet.socialservices.socialservice.Location;
import de.hoogvliet.socialservices.socialservice.LocationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service @Log4j2 @RequiredArgsConstructor
public class LocationMaintenanceService {

    private final LocationRepository locationRepository;
    private final TSVColumnParser tsvColumnParser;

    public Location createOrUpdateLocation(String[] columns) {
        Location locationDb = getLocation(columns);
        return (locationDb == null)?
                createLocation(columns):
                updateLocation(columns, locationDb);
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
