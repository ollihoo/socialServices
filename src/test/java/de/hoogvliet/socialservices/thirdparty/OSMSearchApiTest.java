package de.hoogvliet.socialservices.thirdparty;

import de.hoogvliet.socialservices.osm.OSMSearchApi;
import de.hoogvliet.socialservices.osm.OsmLocation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class OSMSearchApiTest {

    @InjectMocks
    private OSMSearchApi osmSearchApi;

    @Test
    void getsAnswerFromOSM() throws IOException, InterruptedException, URISyntaxException {
        List<OsmLocation> res = osmSearchApi.getOsmData("Friedrichstr. 109", "10117", "Berlin");
        assertEquals("Berlin", res.getFirst().getCity());
        assertEquals("109", res.getFirst().getHouseNumber());
        assertEquals("Friedrichstraße", res.getFirst().getRoad());
        assertEquals("10117", res.getFirst().getPostcode());
        assertEquals("Deutschland", res.getFirst().getCountry());
    }

}
