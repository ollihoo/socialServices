package de.hoogvliet.socialservices.socialservice;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

import java.net.URL;
import java.util.HashSet;

@Getter @Setter @Entity
public class Location {
    @Id
    private String id;
    private String name;
    private String address;
    private String postCode;
    private String city;
    private URL website;

    @OneToMany(mappedBy = "id")
    private HashSet<Category> categories;
}
