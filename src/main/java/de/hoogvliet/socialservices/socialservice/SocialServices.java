package de.hoogvliet.socialservices.socialservice;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

@Service
public class SocialServices {

    public String getAllEntries() {
        ClassPathResource resource = new ClassPathResource("Beratungsstellen.tsv");
        InputStreamReader reader;
        try {
            reader = new InputStreamReader(resource.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
            return "FALSE";
        }
        try (BufferedReader br = new BufferedReader(reader)) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] columns = line.split("\t");
                Location location = new Location();
                location.setName(columns[1]);
                location.setAddress(columns[2]);
                location.setPostCode(columns[3]);
                location.setCity(columns[4]);
                location.setWebsite(getWebsite(columns));
                location.setCategories(getCategories(columns));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "TRUE";
    }

    private static String getCategories(String[] columns) {
        return (columns.length == 7)? columns[6]: null;
    }

    private static URL getWebsite(String[] columns) {
        try {
            return (columns[5] == null)? null : new URL(columns[5]);
        } catch (MalformedURLException e) {
            return null;
        }
    }
}
