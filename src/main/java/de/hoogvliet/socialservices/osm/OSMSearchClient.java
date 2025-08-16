package de.hoogvliet.socialservices.osm;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Collections;
import java.util.List;

@Service @Slf4j
public class OSMSearchClient {
    private static final String USER_AGENT_IDENTIFIER = "de.locating.services.13353";
    public static final String NOMINATIM_HOST = "nominatim.openstreetmap.org";
    public static final String NOMINATIM_PATH = "/search.php";
    public static final String SCHEME = "https";
    public static final String USER_INFO = null;
    public static final int NOMINATIM_PORT = 443;


    public List<OsmCity> getOsmCityData(String city) throws IOException, InterruptedException, URISyntaxException {
        URI uri = new URI (
                SCHEME, USER_INFO, NOMINATIM_HOST, NOMINATIM_PORT, NOMINATIM_PATH,
                "city=" + city+ "&country=Germany&format=json&addressdetails=1",
                null
        );

        ObjectMapper mapper = getObjectMapper();
        HttpResponse<String> response = requestOsmData(uri);
        return mapper.readValue(response.body(),
                new TypeReference<>() {
                });

    }

    public List<OsmLocation> getOsmLocationData(String street, String postalCode, String city) throws IOException, InterruptedException, URISyntaxException {
        URI uri = getLocationRequestUri(street, postalCode, city);
        ObjectMapper mapper = getObjectMapper();
        HttpResponse<String> response = requestOsmData(uri);
        return mapper.readValue(response.body(),
                new TypeReference<>() {
                });
    }

    private static HttpResponse<String> requestOsmData(URI uri) throws IOException, InterruptedException {
        HttpRequest request = createRequest(uri);

        try (HttpClient client = HttpClient.newHttpClient()) {
            return client.send(request, HttpResponse.BodyHandlers.ofString());

        } catch (RuntimeException e) {
            log.error("Error during getting osm data: {}", e.getMessage());
            throw e;
        }
    }

    private static ObjectMapper getObjectMapper() {
        return new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    private static HttpRequest createRequest(URI uri) {
        return HttpRequest.newBuilder()
                .uri(uri)
                .header("User-Agent", USER_AGENT_IDENTIFIER)
                .build();
    }

    private static URI getLocationRequestUri(String street, String postalCode, String city) throws URISyntaxException {
        return new URI (
              "https", null, NOMINATIM_HOST, 443,
                NOMINATIM_PATH, createQuery(street, postalCode, city), null
        );
    }

    private static String createQuery(String street, String postalCode, String city) {
        return "street=" + street
                + "&postal_code=" + postalCode
                + "&city=" + city
                + "&country=Germany&format=json&addressdetails=1";
    }
}
