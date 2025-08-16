package de.hoogvliet.socialservices.thirdparty;

import de.hoogvliet.socialservices.osm.OSMSearchClient;
import de.hoogvliet.socialservices.osm.OsmCity;
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
public class OSMSearchClientTest {

    @InjectMocks
    private OSMSearchClient osmSearchClient;

    @Test
    void getsAnswerFromOSM() throws IOException, InterruptedException, URISyntaxException {
        List<OsmLocation> res = osmSearchClient.getOsmLocationData("Friedrichstr. 109", "10117", "Berlin");
        assertEquals("Berlin", res.getFirst().getCity());
        assertEquals("109", res.getFirst().getHouseNumber());
        assertEquals("Friedrichstraße", res.getFirst().getRoad());
        assertEquals("10117", res.getFirst().getPostcode());
        assertEquals("Deutschland", res.getFirst().getCountry());
    }

    @Test
    void get_answer_for_a_city() throws IOException, InterruptedException, URISyntaxException {
        List<OsmCity> res = osmSearchClient.getOsmCityData("Frankfurt");
        assertEquals("Frankfurt am Main", res.getFirst().getCity());
        assertEquals("Frankfurt (Oder)", res.get(1).getCity());
    }

}
