package de.hoogvliet.socialservices.socialservice.tsv;

import de.hoogvliet.socialservices.socialservice.Location;
import lombok.Getter;

import java.net.URL;

@Getter
public class LocationBuilder {

    private final Location location;

    public static LocationBuilder getInstance(Location existingLocation) {
        if (existingLocation != null) {
            return new LocationBuilder(existingLocation);
        }
            return new LocationBuilder();
    }

    private LocationBuilder() {
        this.location = new Location();
    }

    private LocationBuilder(Location location) {
        this.location = location;
    }

    public LocationBuilder withTableReference(String tableReference) {
        location.setTableReference(tableReference);
        return this;
    }

    public LocationBuilder withName(String value) {
        location.setName(value.trim());
        return this;
    }

    public LocationBuilder withAddress(String value) {
        location.setAddress(value.trim());
        return this;
    }

    public LocationBuilder withPostCode(String value) {
        location.setPostCode(value.trim());
        return this;
    }

    public LocationBuilder withCity(String value) {
        location.setCity(value.trim());
        return this;
    }

    public LocationBuilder withWebsite(URL value) {
        location.setWebsite(value);
        return this;
    }

}
