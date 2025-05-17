package de.hoogvliet.socialservices.osm;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

@Service
public class OSMSearchClient {
    private static final String USER_AGENT_IDENTIFIER = "de.locating.services.13353";
    public static final String NOMINATIM_HOST = "nominatim.openstreetmap.org";
    public static final String NOMINATIM_PATH = "/search.php";


    public List<OsmLocation> getOsmData(String street, String postalCode, String city) throws IOException, InterruptedException, URISyntaxException {
        URI uri = getRequestUri(street, postalCode, city);
        HttpRequest request = createRequest(uri);

        try (HttpClient client = HttpClient.newHttpClient()) {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            ObjectMapper mapper = new ObjectMapper()
                    .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            return mapper.readValue(response.body(),
                    new TypeReference<>() {});
        }
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
