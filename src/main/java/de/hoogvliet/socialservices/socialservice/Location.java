package de.hoogvliet.socialservices.socialservice;

import lombok.Getter;
import lombok.Setter;

import java.net.URL;

@Getter @Setter
public class Location {
    private String name;
    private String address;
    private String postCode;
    private String city;
    private URL website;
    private String categories;
}
