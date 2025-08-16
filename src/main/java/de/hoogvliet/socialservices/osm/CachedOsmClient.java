package de.hoogvliet.socialservices.osm;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service @RequiredArgsConstructor @Slf4j
public class CachedOsmClient {
    private final OSMSearchClient osmSearchClient;
    private final OsmMapper osmMapper;
    private final CacheConfiguration cacheConfig;

    public boolean locationHasBeenParsed(String street, String postalCode, String city) {
        Resource cacheFile = cacheConfig.getCacheResource(street, postalCode, city);
        return cacheFile.exists();
    }

    public List<OsmLocation> getOsmLocations(String street, String postalCode, String city) {
        Resource cacheFile = cacheConfig.getCacheResource(street, postalCode, city);
        if (cacheFile.exists()) {
            return getOsmLocationFromCache(cacheFile);
        } else {
            try {
                List<OsmLocation> osmLocation = getOsmLocationFromClient(street, postalCode, city);
                osmMapper.write(osmLocation, cacheFile);
                return osmLocation;
            } catch (IOException e) {
                throw new OsmException("Can't read data from OSM", e);
            }
        }
    }

    public List<OsmCity> getOsmCities(String city) {
        Resource cacheFile = cacheConfig.getCacheResource(city);
        if (cacheFile.exists()) {
            return getOsmCitiesFromCache(cacheFile);
        } else {
            try {
                List<OsmCity> osmCities = getOsmCitiesFromClient(city);
                osmMapper.write(osmCities, cacheFile);
                return osmCities;
            } catch (IOException e) {
                throw new OsmException("Can't read data from OSM", e);
            }
        }
    }

    private List<OsmCity> getOsmCitiesFromClient(String city) {
        try {
            return osmSearchClient.getOsmCities(city);
        } catch (Exception e) {
            log.warn("Unable to get OSM CITY for {}. Error: {}", city, e.getMessage());
            throw new RuntimeException(e);
        }
    }


    private List<OsmLocation> getOsmLocationFromClient(String street, String postalCode, String city) {
        try {
            return osmSearchClient.getOsmLocations(street, postalCode, city);
        } catch (Exception e) {
            log.warn("Unable to get OSM LOCATION for {},{}. Error: {}", street, city, e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private List<OsmLocation> getOsmLocationFromCache(Resource cacheFile) {
        try {
            return osmMapper.getLocations(cacheFile);
        } catch (IOException e) {
            log.warn("Couldn't use cache file {}. Error: {}", cacheFile.getFilename(), e.getMessage());
            throw new OsmException("There is a problem with the cache file", e);
        }
    }

    private List<OsmCity> getOsmCitiesFromCache(Resource cacheFile) {
        try {
            return osmMapper.getCities(cacheFile);
        } catch (IOException e) {
            log.warn("Couldn't use cache file {}. Error: {}", cacheFile.getFilename(), e.getMessage());
            throw new OsmException("There is a problem with the cache file", e);
        }
    }


}