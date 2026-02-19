package de.hoogvliet.socialservices.thirdparty;

import de.hoogvliet.socialservices.osm.OSMSearchClient;
import de.hoogvliet.socialservices.osm.OsmLocation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URISyntaxException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class OSMSearchClientTest {

    @InjectMocks
    private OSMSearchClient osmSearchClient;

    @BeforeEach()
    void setup () throws NoSuchFieldException, IllegalAccessException {
        Field userAgentIdentifier = OSMSearchClient.class.getDeclaredField("USER_AGENT_IDENTIFIER");
        userAgentIdentifier.setAccessible(true);
        userAgentIdentifier.set(osmSearchClient, "de.locating.services.test.13353");
    }
    
    @Test
    void getsAnswerFromOSM() throws IOException, InterruptedException, URISyntaxException {

        List<OsmLocation> res = osmSearchClient.getOsmLocations("Friedrichstr. 109", "10117", "Berlin");
        assertEquals("Berlin", res.getFirst().getCity());
        assertEquals("109", res.getFirst().getHouseNumber());
        assertEquals("Friedrichstraße", res.getFirst().getRoad());
        assertEquals("10117", res.getFirst().getPostcode());
        assertEquals("Deutschland", res.getFirst().getCountry());
    }

}
