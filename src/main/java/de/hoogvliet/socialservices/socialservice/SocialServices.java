package de.hoogvliet.socialservices.socialservice;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

@Service
public class SocialServices {
    private static final int COLUMN_NAME = 1;
    private static final int COLUMN_ADRESS = 2;
    private static final int COLUMN_POSTCODE = 3;
    private static final int COLUMN_CITY = 4;
    private static final int COLUMN_WEBSITE = 5;
    private static final int COLUMN_CATEGORIES = 6;

    public List<Location> getAllEntries() {
        ClassPathResource resource = new ClassPathResource("Beratungsstellen.tsv");
        List<Location> locations = new ArrayList<>();
        InputStreamReader reader;
        try {
            reader = new InputStreamReader(resource.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
            return locations;
        }
        try (BufferedReader br = new BufferedReader(reader)) {
            String line;
            while ((line = br.readLine()) != null) {
                if (! line.startsWith("Zeitstempel")) {
                    String[] columns = line.split("\t");
                    locations.add(createLocation(columns));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return locations;
    }

    public HashSet<Category> getCategories(List<Location> locations) {
        HashSet<Category> categories = new HashSet<>();
        locations.forEach(location -> categories.addAll(location.getCategories()));
        return categories;
    }


    private static Location createLocation(String[] columns) {
        Location location = new Location();
        location.setId(hashString(columns[0]));
        location.setName(columns[COLUMN_NAME]);
        location.setAddress(columns[COLUMN_ADRESS]);
        location.setPostCode(columns[COLUMN_POSTCODE]);
        location.setCity(columns[COLUMN_CITY]);
        location.setWebsite(getWebsite(columns));
        location.setCategories(getCategories(columns));
        return location;
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

    private static HashSet<Category> getCategories(String[] entry) {
        HashSet<Category> categorySet = new HashSet<>();
        if (entry.length >= 7) {
            String[] categories = entry[COLUMN_CATEGORIES].split(", ?");
            Arrays.stream(categories).forEach((String cat) -> {
                Category myCat = new Category();
                myCat.setName(cat);
                categorySet.add(myCat);
            });
        }
        return categorySet;
    }

    private static URL getWebsite(String[] entry) {
        try {
            return (entry[COLUMN_WEBSITE] == null)? null : new URL(entry[COLUMN_WEBSITE]);
        } catch (MalformedURLException e) {
            return null;
        }
    }
}
