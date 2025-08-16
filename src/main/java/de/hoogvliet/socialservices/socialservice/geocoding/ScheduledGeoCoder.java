package de.hoogvliet.socialservices.socialservice.geocoding;

import de.hoogvliet.socialservices.osm.CachedOsmClient;
import de.hoogvliet.socialservices.osm.OsmLocation;
import de.hoogvliet.socialservices.socialservice.Location;
import de.hoogvliet.socialservices.socialservice.LocationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component @RequiredArgsConstructor @Slf4j
public class ScheduledGeoCoder {

    private final LocationRepository locationRepository;
    private final CachedOsmClient osmSearchClient;

    private final List<String> ACCEPTABLE_LOC_TYPES = List.of(
            "house", "library", "company", "doctors", "psychotherapist", "community_centre",
            "social_facility", "place_of_worship", "townhall"
    );

    @Scheduled(fixedRate = 200000)
    public void updateLocationWithLatLon () {
        Location loc = getNextParsableLocation();
        if (loc == null) {
            return;
        }

        try {
            List<OsmLocation> osmData = osmSearchClient.getOsmData(loc.getAddress(), loc.getPostCode(), loc.getCity());
            if (osmData.size() == 1) {
                saveLocation(loc, osmData.get(0));
            } else {
                log.warn("Ambiguous data for '{}'. Data was {}, but not 1.", loc.getAddress(), osmData.size());
                for (OsmLocation osmLocation : osmData) {
                    if (ACCEPTABLE_LOC_TYPES.contains(osmLocation.getType())) {
                        saveLocation(loc, osmLocation);
                        break;
                    } else {
                        log.warn("OSM entry is type {} for {} -> lat/lon {},{}",
                                osmLocation.getType(),
                                loc.getName(), osmLocation.getLatitude(), osmLocation.getLongitude());
                    }
                }
            }
        } catch (Exception e) {
            log.warn("Could not drive geo update: {}", e.getMessage());
        }
    }

    private Location getNextParsableLocation() {
        List<Location> locations = locationRepository.getLocationByLatitudeNull();
        for (Location location : locations) {
            if (! osmSearchClient.locationHasBeenParsed(
                    location.getAddress(), location.getPostCode(), location.getCity())) {
                return location;
            }
        }
        return null;
    }

    private void saveLocation (Location loc, OsmLocation osmLocation) {
        loc.setLatitude(osmLocation.getLatitude());
        loc.setLongitude(osmLocation.getLongitude());
        locationRepository.save(loc);
        log.info("Updated location {} with lat/lon: {}/{}",
                loc.getName(), loc.getLatitude(), loc.getLongitude());
    }

}
