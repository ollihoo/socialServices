package de.hoogvliet.socialservices.socialservice;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

@Service
public class LocationCityService {
    private final LocationCityRepository locationCityRepository;

    public LocationCityService(LocationCityRepository repository) {
        this.locationCityRepository = repository;
    }

    public void save(Location location, List<City> cities) {
        cities.forEach(city -> {
            Optional<LocationCity> optionalLocationCity =
                    locationCityRepository.findByLocationIdAndCityId(location.getId(), city.getId());
            if (optionalLocationCity.isEmpty()) {
                createLocationCity(location, city);
            }
        });
    }

    private void createLocationCity(Location location, City city) {
        LocationCity lc = new LocationCity();
        lc.setCity(city);
        lc.setLocation(location);
        locationCityRepository.save(lc);
    }
}
