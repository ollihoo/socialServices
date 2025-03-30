package de.hoogvliet.socialservices.socialservice;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter @Entity
public class Location {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String name;
    @Nonnull @JsonIgnore
    private String tableReference;
    private String address;
    private String postCode;
    private String city;
    private URL website;
    @JsonIgnore
    @OneToMany(mappedBy = "location")
    private List<LocationCategory> locationCategories = new ArrayList<>();
}
