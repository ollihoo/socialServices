package de.hoogvliet.socialservices.socialservice;

import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

@Service
public class LocationService {
    private static final int COLUMN_NAME = 1;
    private static final int COLUMN_ADRESS = 2;
    private static final int COLUMN_POSTCODE = 3;
    private static final int COLUMN_CITY = 4;
    private static final int COLUMN_WEBSITE = 5;
    private final LocationRepository locationRepository;

    public LocationService(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    public Location createLocation(String id, String[] columns) {
        Location location = new Location();
        location.setTableReference(id);
        location.setName(columns[COLUMN_NAME]);
        location.setAddress(columns[COLUMN_ADRESS]);
        location.setPostCode(columns[COLUMN_POSTCODE]);
        location.setCity(columns[COLUMN_CITY]);
        location.setWebsite(getWebsite(columns));
        locationRepository.save(location);
        return location;
    }

    public Location getOrCreateLocation(String[] columns) {
        String id = hashString(columns[0]);
        Optional<Location> locationOptional = locationRepository.findByTableReference(id);
        return locationOptional.orElseGet(() -> createLocation(id, columns));
    }

    private static URL getWebsite(String[] entry) {
        try {
            return (entry[COLUMN_WEBSITE] == null)? null : new URL(entry[COLUMN_WEBSITE]);
        } catch (MalformedURLException e) {
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
