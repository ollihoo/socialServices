package de.hoogvliet.socialservices.osm;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.jackson.JsonComponent;

import java.util.List;

@JsonComponent @Getter @Setter
public class OsmCity extends OsmLocation {

    @JsonProperty("boundingbox")
    private List<Double> boundingbox;

}
