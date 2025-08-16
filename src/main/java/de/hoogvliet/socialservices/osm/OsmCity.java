package de.hoogvliet.socialservices.osm;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.jackson.JsonComponent;

import java.util.List;
import java.util.Map;

@JsonComponent @Getter @Setter
public class OsmCity extends OsmResponse {
    @JsonProperty("address")
    protected Map<String, String> address;

    @JsonProperty("boundingbox")
    private List<Double> boundingbox;

    public String getCity() {
        return address.get("city");
    }

}
