package de.hoogvliet.socialservices.osm;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CacheConfigurationTest {
    private static final String ANY_STREET = "Straße 34";
    private static final String ANY_POSTAL_CODE = "13353 AD";
    private static final String ANY_CITY = "Berlin/Wedding";

    @InjectMocks
    private CacheConfiguration cacheConfiguration;
    @Mock
    private ResourceLoader resourceLoader;
    @Mock
    private Resource resource;

    @BeforeEach
    void setup() {
        ReflectionTestUtils.setField(cacheConfiguration, "OSM_CACHE_PATH", "/mypath");
    }

    @Test
    void cacheFileIsSetProperly() {
        when(resourceLoader.getResource(anyString())).thenReturn(resource);
        cacheConfiguration.getCacheResource(ANY_STREET, ANY_POSTAL_CODE, ANY_CITY);
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(resourceLoader).getResource(captor.capture());
        assertEquals("/mypath/BerlinWedding_13353AD_Straße34.json", captor.getValue());
    }

}