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

    @JsonProperty
    private Double latitude;
    
    @JsonProperty
    private Double longitude;
}
