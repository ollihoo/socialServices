package de.hoogvliet.socialservices.socialservice;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LocationRepository extends JpaRepository<Location, Long> {
    Optional<Location> findByTableReference(String id);
}

