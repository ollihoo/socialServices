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


    public List<OsmCity> getOsmData(String city) throws IOException, InterruptedException, URISyntaxException {
        URI uri = new URI (
                SCHEME, USER_INFO, NOMINATIM_HOST, NOMINATIM_PORT, NOMINATIM_PATH,
                "city=" + city+ "&country=Germany&format=json&addressdetails=1",
                null
        );
        return requestOsmCity(uri);
    }

    private static List<OsmCity> requestOsmCity(URI uri) throws IOException, InterruptedException {
        HttpRequest request = createRequest(uri);

        HttpClient client = HttpClient.newHttpClient();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            ObjectMapper mapper = getObjectMapper();
            return mapper.readValue(response.body(),
                    new TypeReference<>() {
                    });
        } catch (RuntimeException e) {
            log.error("Error during getting osm data: {}", e.getMessage());
            return Collections.emptyList();
        }
    }

    public List<OsmLocation> getOsmData(String street, String postalCode, String city) throws IOException, InterruptedException, URISyntaxException {
        URI uri = getRequestUri(street, postalCode, city);
        return requestOsmData(uri);
    }

    private static List<OsmLocation> requestOsmData(URI uri) throws IOException, InterruptedException {
        HttpRequest request = createRequest(uri);

        HttpClient client = HttpClient.newHttpClient();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            ObjectMapper mapper = getObjectMapper();
            return mapper.readValue(response.body(),
                    new TypeReference<>() {
                    });
        } catch (RuntimeException e) {
            log.error("Error during getting osm data: {}", e.getMessage());
            return Collections.emptyList();
        }
    }

    private static ObjectMapper getObjectMapper() {
        ObjectMapper mapper = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper;
    }

    private static HttpRequest createRequest(URI uri) {
        return HttpRequest.newBuilder()
                .uri(uri)
                .header("User-Agent", USER_AGENT_IDENTIFIER)
                .build();
    }

    private static URI getRequestUri(String street, String postalCode, String city) throws URISyntaxException {
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
