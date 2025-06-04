package de.hoogvliet.socialservices.osm;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.jackson.JsonComponent;

import java.util.Map;

@JsonComponent @Getter @Setter
public class OsmLocation {
    @JsonProperty("osm_id")
    private Long osmId;

    @JsonProperty
    private String type;

    @JsonProperty
    private Double lat;
    @JsonProperty
    private Double lon;

    @JsonProperty("address")
    private Map<String, String> address;

    public String getHouseNumber() {
        return address.get("house_number");
    }

    public String getRoad() {
        return address.get("road");
    }

    public String getCity() {
        return address.get("city");
    }

    public String getPostcode() {
        return address.get("postcode");
    }

    public String getCountry() {
        return address.get("country");
    }

}
