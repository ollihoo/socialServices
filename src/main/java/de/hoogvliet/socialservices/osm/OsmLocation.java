package de.hoogvliet.socialservices.osm;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.jackson.JacksonComponent;

@JacksonComponent
@Getter @Setter
public class OsmLocation extends OsmCity {

    public String getHouseNumber() {
        return address.get("house_number");
    }

    public String getRoad() {
        return address.get("road");
    }

    public String getPostcode() {
        return address.get("postcode");
    }

    public String getCountry() {
        return address.get("country");
    }

}
