package de.hoogvliet.socialservices.socialservice;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter @Setter @Entity
public class City {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NonNull
    private String name;

    @JsonIgnore
    @OneToMany(mappedBy = "city")
    private List<LocationCity> locationCities = new ArrayList<>();
}