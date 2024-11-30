package de.hoogvliet.socialservices.socialservice.tsv;

import de.hoogvliet.socialservices.socialservice.Location;
import de.hoogvliet.socialservices.socialservice.LocationRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URI;
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

    public Location createLocation(String tableReference, String[] columns) {
        Location location = new Location();
        location.setTableReference(tableReference);
        location.setName(columns[COLUMN_NAME.getColum()]);
        location.setAddress(columns[COLUMN_ADRESS.getColum()]);
        location.setPostCode(columns[COLUMN_POSTCODE.getColum()]);
        location.setCity(columns[COLUMN_CITY.getColum()]);
        location.setWebsite(getWebsite(columns));
        locationRepository.save(location);
        return location;
    }

    public Location getOrCreateLocation(String[] columns) {
        String tableReference = hashString(columns[COLUMN_TIMESTAMP.getColum()]);
        Optional<Location> locationOptional = locationRepository.findByTableReference(tableReference);
        return locationOptional.orElseGet(() -> createLocation(tableReference, columns));
    }

    private static URL getWebsite(String[] entry) {
        if (entry.length <= COLUMN_WEBSITE.getColum()) {
            return null;
        }
        try {
            URI uri = URI.create(entry[COLUMN_WEBSITE.getColum()]);//.toURL();
            return uri.toURL();
        } catch (IllegalArgumentException | MalformedURLException e) {
            log.warn("Please check tsv input: {}", e.getMessage());
            return null;
        }
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
