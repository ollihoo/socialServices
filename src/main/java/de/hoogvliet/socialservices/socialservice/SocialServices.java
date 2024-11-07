package de.hoogvliet.socialservices.socialservice;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

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

                for (String column : columns) {
                    System.out.print(column + " ");
                }
                System.out.println();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "TRUE";
    }
}
