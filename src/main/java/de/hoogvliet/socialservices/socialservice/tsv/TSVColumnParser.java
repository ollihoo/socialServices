package de.hoogvliet.socialservices.socialservice.tsv;

import de.hoogvliet.socialservices.socialservice.Location;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Service @Slf4j
public class TSVColumnParser {
    private static final int COLUMN_TIMESTAMP = 0;
    private static final int COLUMN_NAME = 1;
    private static final int COLUMN_ADDRESS = 2;
    private static final int COLUMN_POSTCODE = 3;
    private static final int COLUMN_CITY = 4;
    private static final int COLUMN_WEBSITE = 5;

    public Location createLocation(String[] columns) {
        return buildLocation(columns, new LocationBuilder());
    }

    public Location updateLocation(String[] columns, Location existingLocation) {
        return buildLocation(columns, new LocationBuilder(existingLocation));
    }

    private Location buildLocation(String[] columns, LocationBuilder locationBuilder) {
        return locationBuilder
                .withTableReference(createTableReference(columns))
                .withName(columns[COLUMN_NAME])
                .withAddress(columns[COLUMN_ADDRESS])
                .withPostCode(columns[COLUMN_POSTCODE])
                .withCity(columns[COLUMN_CITY])
                .withWebsite(getWebsite(columns))
                .getLocation();
    }

    public String createTableReference(String[] columns) {
        return hashString(columns[COLUMN_TIMESTAMP]);
    }

    private static String hashString(String input) {
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        byte[] hashBytes = digest.digest(input.getBytes());
        StringBuilder hexString = new StringBuilder();
        for (byte b : hashBytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }

    private static URL getWebsite(String[] entry) {
        if (entry.length <= COLUMN_WEBSITE) {
            return null;
        }
        try {
            URI uri = URI.create(entry[COLUMN_WEBSITE]);
            return uri.toURL();
        } catch (IllegalArgumentException | MalformedURLException e) {
            log.warn("Please check tsv input: {}", e.getMessage());
            return null;
        }
    }
}
