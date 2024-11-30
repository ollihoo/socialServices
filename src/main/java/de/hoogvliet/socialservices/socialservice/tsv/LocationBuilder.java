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

    public LocationBuilder withName(String column) {
        location.setName(column);
        return this;
    }

    public LocationBuilder withAddress(String column) {
        location.setAddress(column);
        return this;
    }

    public LocationBuilder withPostCode(String column) {
        location.setPostCode(column);
        return this;
    }

    public LocationBuilder withCity(String column) {
        location.setCity(column);
        return this;
    }

    public LocationBuilder withWebsite(URL column) {
        location.setWebsite(column);
        return this;
    }

    public Location getLocation() {
        return location;
    }

}
