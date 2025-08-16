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
    void when_cache_fails_get_data_from_getOsmLocationData() throws IOException, URISyntaxException, InterruptedException {
        when(cachedResource.exists()).thenReturn(false);
        when(cacheConfiguration.getCacheResource(anyString(), anyString(), anyString())).thenReturn(cachedResource);
        cachedOsmClient.getOsmData(ANY_STREET, ANY_POSTAL_CODE, ANY_CITY);
        verify(osmSearchClient).getOsmLocationData(ANY_STREET, ANY_POSTAL_CODE, ANY_CITY);
    }

    @Test
    void when_cache_fails_response_Is_written_into_cache_file() throws IOException, URISyntaxException, InterruptedException {
        List<OsmLocation> anyData = Collections.emptyList();
        when(cachedResource.exists()).thenReturn(false);
        when(cacheConfiguration.getCacheResource(anyString(), anyString(), anyString())).thenReturn(cachedResource);
        when(osmSearchClient.getOsmLocationData(anyString(), anyString(), anyString())).thenReturn(anyData);
        cachedOsmClient.getOsmData(ANY_STREET, ANY_POSTAL_CODE, ANY_CITY);
        verify(osmMapper).write(anyData, cachedResource);
    }

    @Test
    void whenCacheHits_DontUseOsmSearchClient() throws IOException, URISyntaxException, InterruptedException {
        when(cachedResource.exists()).thenReturn(true);
        when(cacheConfiguration.getCacheResource(anyString(), anyString(), anyString())).thenReturn(cachedResource);
        cachedOsmClient.getOsmData(ANY_STREET, ANY_POSTAL_CODE, ANY_CITY);
        verify(osmSearchClient, never()).getOsmLocationData(ANY_STREET, ANY_POSTAL_CODE, ANY_CITY);
    }

    @Test
    void whenCacheHits_GetDataFromOsmCache() throws IOException {
        when(cachedResource.exists()).thenReturn(true);
        when(cacheConfiguration.getCacheResource(anyString(), anyString(), anyString())).thenReturn(cachedResource);
        cachedOsmClient.getOsmData(ANY_STREET, ANY_POSTAL_CODE, ANY_CITY);
        verify(osmMapper).get(cachedResource);
    }

}
