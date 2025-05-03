package de.hoogvliet.socialservices.socialservice.tsv;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import de.hoogvliet.socialservices.socialservice.City;
import de.hoogvliet.socialservices.socialservice.CityRepository;

@Component
public class TSVCityParser {
    private static final int COLUMN_CITY = 4;
    @Autowired private CityRepository cityRepository;

    public List<City> getOrCreateCity(String[] entry) {
        List<City> city = new ArrayList<>();
        city.add(searchCityByName(entry[COLUMN_CITY]));
        return city;
    }

    private City searchCityByName(String cityName) {
        Optional<City> optionalCity = cityRepository.findByName(cityName);
        return optionalCity.orElseGet(() -> createCity(cityName));
    }

    private City createCity(String cityName) {
        City city = new City();
        city.setName(cityName);
        cityRepository.save(city);
        return city;
    }
    
}