package de.hoogvliet.socialservices.osm;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

@Service @Slf4j
public class OSMSearchClient {
    @Value("${application.osm.useragent.identifier}")
    private String USER_AGENT_IDENTIFIER;
    public static final String NOMINATIM_HOST = "nominatim.openstreetmap.org";
    public static final String NOMINATIM_PATH = "/search.php";
    public static final String SCHEME = "https";
    public static final String USER_INFO = null;
    public static final int NOMINATIM_PORT = 443;


    public List<OsmCity> getOsmCities(String city) throws IOException, InterruptedException, URISyntaxException {
        URI uri = getCityRequestUri(city);
        HttpResponse<String> response = getOsmResponse(uri);
        return getObjectMapper().readValue(response.body(), new TypeReference<>() {});
    }

    public List<OsmLocation> getOsmLocations(String street, String postalCode, String city) throws IOException, InterruptedException, URISyntaxException {
        URI uri = getLocationRequestUri(street, postalCode, city);
        HttpResponse<String> response = getOsmResponse(uri);
        return getObjectMapper().readValue(response.body(), new TypeReference<>() {});
    }

    private HttpResponse<String> getOsmResponse(URI uri) throws IOException, InterruptedException {
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

    private HttpRequest createRequest(URI uri) {
        return HttpRequest.newBuilder()
                .uri(uri)
                .header("User-Agent", USER_AGENT_IDENTIFIER)
                .build();
    }

    private static URI getCityRequestUri(String city) throws URISyntaxException {
        return new URI(
                SCHEME, USER_INFO, NOMINATIM_HOST, NOMINATIM_PORT, NOMINATIM_PATH,
                "city=" + city + "&country=Germany&format=json&addressdetails=1",
                null
        );
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
