package de.hoogvliet.socialservices.socialservice.tsv;

import de.hoogvliet.socialservices.socialservice.Location;
import de.hoogvliet.socialservices.socialservice.LocationRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

import static de.hoogvliet.socialservices.socialservice.tsv.TSVDescription.*;

@Service @Log4j2
public class LocationMaintenanceService {
    private final LocationRepository locationRepository;

    public LocationMaintenanceService(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    public Location createOrUpdateLocation(String[] columns) {
        String tableReference = createTableReference(columns);
        Location locationFromDb = getLocation(tableReference);
        if (locationFromDb == null) {
            return setAndSaveLocation(columns, LocationBuilder.getInstance(null).withTableReference(tableReference));
        } else {
            return setAndSaveLocation(columns, LocationBuilder.getInstance(locationFromDb));
        }

    }
    private Location getLocation(String tableReference) {
        Optional<Location> locationOptional = locationRepository.findByTableReference(tableReference);
        return locationOptional.orElse(null);
    }

    private Location setAndSaveLocation(String[] columns, LocationBuilder locationBuilder) {
        Location location = locationBuilder
                .withName(columns[COLUMN_NAME.getColum()])
                .withAddress(columns[COLUMN_ADRESS.getColum()])
                .withPostCode(columns[COLUMN_POSTCODE.getColum()])
                .withCity(columns[COLUMN_CITY.getColum()])
                .withWebsite(getWebsite(columns))
                .getLocation();
        locationRepository.save(location);
        return location;
    }

    private static URL getWebsite(String[] entry) {
        if (entry.length <= COLUMN_WEBSITE.getColum()) {
            return null;
        }
        try {
            URI uri = URI.create(entry[COLUMN_WEBSITE.getColum()]);
            return uri.toURL();
        } catch (IllegalArgumentException | MalformedURLException e) {
            log.warn("Please check tsv input: {}", e.getMessage());
            return null;
        }
    }

    private static String createTableReference(String[] columns) {
        return hashString(columns[COLUMN_TIMESTAMP.getColum()]);
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
