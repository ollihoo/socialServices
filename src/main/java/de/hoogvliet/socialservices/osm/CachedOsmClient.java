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
        Resource cacheResource = cacheConfig.getCacheResource(street, postalCode, city);
        return cacheResource.exists();
    }

    public List<OsmLocation> getOsmData(String street, String postalCode, String city) {
        Resource resource = cacheConfig.getCacheResource(street, postalCode, city);
        if (resource.exists()) {
            try {
                return osmMapper.get(resource);
            } catch (IOException e) {
                log.warn("Couldn't use file {}. Error: {}", resource.getFilename(), e.getMessage());
                throw new RuntimeException(e);
            }
        } else {
            try {
                List<OsmLocation> osmData = osmSearchClient.getOsmData(street, postalCode, city);
                osmMapper.write(osmData, resource);
                return osmData;
            } catch (Exception e) {
                log.warn("Unable to get OSM data for {},{}. Error: {}", street, city, e.getMessage());
                log.warn(e.toString());
                throw new RuntimeException(e);
            }
        }
    }



}