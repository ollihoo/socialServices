package de.hoogvliet.socialservices.socialservice;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Integer> {

    Optional<Category> findByName(String name);
    
    Optional<Category> findByNameIgnoreCase(String name);

    List<Category> findAllByOrderByNameAsc();
}
