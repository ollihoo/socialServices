package de.hoogvliet.socialservices.socialservice.tsv;

import de.hoogvliet.socialservices.socialservice.Location;
import de.hoogvliet.socialservices.socialservice.LocationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

@Service @Log4j2 @RequiredArgsConstructor
public class LocationMaintenanceService {
    private static final int COLUMN_TIMESTAMP = 0;
    private static final int COLUMN_NAME = 1;
    private static final int COLUMN_ADRESS = 2;
    private static final int COLUMN_POSTCODE = 3;
    private static final int COLUMN_CITY = 4;
    private static final int COLUMN_WEBSITE = 5;
    private final LocationRepository locationRepository;

    public Location createOrUpdateLocation(String[] columns) {
        String tableReference = createTableReference(columns);
        Location locationFromDb = getLocation(tableReference);
        LocationBuilder builder = LocationBuilder.getInstance(locationFromDb).withTableReference(tableReference);
        return setAndSaveLocation(columns, builder);
    }

    private Location getLocation(String tableReference) {
        Optional<Location> locationOptional = locationRepository.findByTableReference(tableReference);
        return locationOptional.orElse(null);
    }

    private Location setAndSaveLocation(String[] columns, LocationBuilder locationBuilder) {
        Location location = locationBuilder
                .withName(columns[COLUMN_NAME])
                .withAddress(columns[COLUMN_ADRESS])
                .withPostCode(columns[COLUMN_POSTCODE])
                .withCity(columns[COLUMN_CITY])
                .withWebsite(getWebsite(columns))
                .getLocation();
        locationRepository.save(location);
        return location;
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

    private static String createTableReference(String[] columns) {
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

}
