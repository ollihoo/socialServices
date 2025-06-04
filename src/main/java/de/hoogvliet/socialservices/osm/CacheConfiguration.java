package de.hoogvliet.socialservices.osm;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

@Component @RequiredArgsConstructor
public class CacheConfiguration {
    private final ResourceLoader resourceLoader;

    @Value("${application.osm.cache.path}")
    private String OSM_CACHE_PATH;

    public Resource getCacheResource(String street, String postalCode, String city) {
        String cacheFileName = getCacheFileName(street, postalCode, city);
        return resourceLoader.getResource(OSM_CACHE_PATH + "/" + cacheFileName);
    }

    private static String getCacheFileName(String street, String postalCode, String city) {
        String name = city + "_" + postalCode + "_" + street + ".json";
        return name.replaceAll("/", "").replaceAll(" ", "");
    }

}
