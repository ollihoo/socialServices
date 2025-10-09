package de.hoogvliet.socialservices.socialservice.geocoding;

import de.hoogvliet.socialservices.osm.CachedOsmClient;
import de.hoogvliet.socialservices.osm.OSMSearchClient;
import de.hoogvliet.socialservices.osm.OsmCity;
import de.hoogvliet.socialservices.socialservice.City;
import de.hoogvliet.socialservices.socialservice.CityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component @RequiredArgsConstructor @Slf4j
public class ScheduledCityGeoCoder {

    private final CityRepository cityRepository;
    private final CachedOsmClient osmSearchClient;

    @Scheduled(fixedRate = 2000)
    public void updateCityWithLatLon() {
        List<City> cities = cityRepository.findByLatitudeNullOrLongitudeNull();
        for (City city : cities) {
            try {
                List<OsmCity> osmCities = osmSearchClient.getOsmCities(city.getName());
                if (osmCities.isEmpty()) {
                    log.warn(city.getName() + " not found");
                    return;
                } else if (osmCities.size() == 1) {
                    OsmCity osmCity = osmCities.getFirst();
                    city.setLatitude(osmCity.getLatitude());
                    city.setLongitude(osmCity.getLongitude());
                    cityRepository.save(city);
                } else {
                    log.warn("{} produces {} entries", city.getName(), osmCities.size());
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
