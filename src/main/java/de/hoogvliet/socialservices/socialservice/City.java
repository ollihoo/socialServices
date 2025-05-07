package de.hoogvliet.socialservices.socialservice;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter @Setter @Entity
public class City {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NonNull
    private String name;
}