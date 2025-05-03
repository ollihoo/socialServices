package de.hoogvliet.socialservices.socialservice;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service @RequiredArgsConstructor
public class CityService {
    private final CityRepository cityRepository;

    public City saveCity(String cityName) {
        Optional<City> cityByName = cityRepository.findByName(cityName);
        if (cityByName.isEmpty()) {
            City city = new City();
            city.setName(cityName);
            cityRepository.save(city);
            return city;
        }
        return cityByName.get();
    }

}
