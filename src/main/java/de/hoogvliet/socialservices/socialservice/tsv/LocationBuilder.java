package de.hoogvliet.socialservices.socialservice.tsv;

import de.hoogvliet.socialservices.socialservice.Location;
import java.net.URL;

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
        location.setName(value);
        return this;
    }

    public LocationBuilder withAddress(String value) {
        location.setAddress(value);
        return this;
    }

    public LocationBuilder withPostCode(String value) {
        location.setPostCode(value);
        return this;
    }

    public LocationBuilder withCity(String value) {
        location.setCity(value);
        return this;
    }

    public LocationBuilder withWebsite(URL value) {
        location.setWebsite(value);
        return this;
    }

    public Location getLocation() {
        return location;
    }

}
