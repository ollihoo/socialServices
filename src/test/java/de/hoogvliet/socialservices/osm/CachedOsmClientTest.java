package de.hoogvliet.socialservices.osm;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CachedOsmClientTest {
    private static final String ANY_STREET = "Straße 34";
    private static final String ANY_POSTAL_CODE = "13353 AD";
    private static final String ANY_CITY = "Berlin/Wedding";
    @InjectMocks
    private CachedOsmClient cachedOsmClient;
    @Mock
    private OSMSearchClient osmSearchClient;
    @Mock
    private CacheConfiguration cacheConfiguration;
    @Mock
    private Resource cachedResource;
    @Mock
    private OsmMapper osmMapper;

    @Test
    void when_location_cache_fails_get_data_from_getOsmLocationsData() throws IOException, URISyntaxException, InterruptedException {
        when(cachedResource.exists()).thenReturn(false);
        when(cacheConfiguration.getCacheResource(anyString(), anyString(), anyString())).thenReturn(cachedResource);
        cachedOsmClient.getOsmLocations(ANY_STREET, ANY_POSTAL_CODE, ANY_CITY);
        verify(osmSearchClient).getOsmLocations(ANY_STREET, ANY_POSTAL_CODE, ANY_CITY);
        verify(osmMapper, never()).getLocations(cachedResource);
    }

    @Test
    void when_location_cache_fails_response_is_written_into_cache_file() throws IOException, URISyntaxException, InterruptedException {
        List<OsmLocation> anyData = Collections.emptyList();
        when(cachedResource.exists()).thenReturn(false);
        when(cacheConfiguration.getCacheResource(anyString(), anyString(), anyString())).thenReturn(cachedResource);
        when(osmSearchClient.getOsmLocations(anyString(), anyString(), anyString())).thenReturn(anyData);
        cachedOsmClient.getOsmLocations(ANY_STREET, ANY_POSTAL_CODE, ANY_CITY);
        verify(osmMapper).write(anyData, cachedResource);
    }

    @Test
    void when_location_cache_hits_get_data_from_cache() throws IOException, URISyntaxException, InterruptedException {
        when(cachedResource.exists()).thenReturn(true);
        when(cacheConfiguration.getCacheResource(anyString(), anyString(), anyString())).thenReturn(cachedResource);
        cachedOsmClient.getOsmLocations(ANY_STREET, ANY_POSTAL_CODE, ANY_CITY);
        verify(osmMapper).getLocations(cachedResource);
        verify(osmSearchClient, never()).getOsmLocations(ANY_STREET, ANY_POSTAL_CODE, ANY_CITY);
    }

    @Test
    void when_city_cache_fails_get_data_from_client() throws IOException, URISyntaxException, InterruptedException {
        when(cachedResource.exists()).thenReturn(false);
        when(cacheConfiguration.getCacheResource(anyString())).thenReturn(cachedResource);
        cachedOsmClient.getOsmCities(ANY_CITY);
        verify(osmSearchClient).getOsmCities(ANY_CITY);
        verify(osmMapper, never()).getCities(cachedResource);

    }

    @Test
    void when_city_cache_hits_get_data_from_cache() throws IOException, URISyntaxException, InterruptedException {
        when(cachedResource.exists()).thenReturn(true);
        when(cacheConfiguration.getCacheResource(anyString())).thenReturn(cachedResource);
        cachedOsmClient.getOsmCities(ANY_CITY);
        verify(osmMapper).getCities(cachedResource);
        verify(osmSearchClient, never()).getOsmCities(ANY_CITY);
    }

}
