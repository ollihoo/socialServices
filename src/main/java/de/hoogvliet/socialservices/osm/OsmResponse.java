package de.hoogvliet.socialservices.osm;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.jackson.JsonComponent;

@JsonComponent @Getter @Setter
public class OsmResponse {
    @JsonProperty("osm_id")
    private Long osmId;

    @JsonProperty
    private String type;

    @JsonProperty("lat")
    private Double latitude;
    
    @JsonProperty("lon")
    private Double longitude;
}
