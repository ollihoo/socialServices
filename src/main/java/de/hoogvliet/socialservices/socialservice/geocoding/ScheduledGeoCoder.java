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

    @Scheduled(fixedRate = 20000)
    public void updateLocationWithLatLon () {
        List<Location> locations = locationRepository.getLocationByLatitudeNull();
        if (locations.isEmpty()) {
            return;
        }
        Location loc = locations.getFirst();
        try {
            List<OsmLocation> osmData = osmSearchClient.getOsmData(loc.getAddress(), loc.getPostCode(), loc.getCity());
            if (osmData.size() == 1) {
                saveLocation(loc, osmData.getFirst());
            } else {
                log.warn("Ambiguous data for '{}'. Data was {}, but not 1.", loc.getAddress(), osmData.size());
                for (OsmLocation osmLocation : osmData) {
                    if ("house".equals(osmLocation.getType()) || "library".equals(osmLocation.getType())) {
                        saveLocation(loc, osmLocation);
                        break;
                    } else {
                        log.warn("OSM entry is type {} for {}", osmLocation.getType(), loc.getName());
                    }
                }
            }
        }
        catch (Exception e) {
            log.warn("Could not drive geo update: {}", e.getMessage());
        }
    }

    private void saveLocation (Location loc, OsmLocation osmLocation) {
        loc.setLatitude(osmLocation.getLat());
        loc.setLongitude(osmLocation.getLon());
        locationRepository.save(loc);
        log.info("Updated location {} with lat/lon: {}/{}",
                loc.getName(), loc.getLatitude(), loc.getLongitude());
    }

}
