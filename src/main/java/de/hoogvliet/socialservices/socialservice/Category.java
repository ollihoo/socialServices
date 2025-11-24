package de.hoogvliet.socialservices.socialservice;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity @Data
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String name;

    @JsonIgnore
    @OneToMany(mappedBy = "category")
    private List<LocationCategory> locationCategories = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (! (o instanceof Category other)) return false;
        return id == other.id && name.equals(other.name);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
