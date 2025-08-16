package de.hoogvliet.socialservices.osm;

public class OsmException extends RuntimeException {

    public OsmException(String message) {
        super(message);
    }

    public OsmException(String message, Throwable cause) {
        super(message, cause);
    }
}
