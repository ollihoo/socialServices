package de.hoogvliet.socialservices.socialservice;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity @Getter @Setter
@Table(uniqueConstraints = @UniqueConstraint(columnNames= {"location_id","city_id"}))
public class LocationCity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "location_id", nullable = false)
    private Location location;

    @ManyToOne
    @JoinColumn(name = "city_id", nullable = false)
    private City city;
}