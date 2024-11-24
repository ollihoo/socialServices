package de.hoogvliet.socialservices.socialservice;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LocationCategoryRepository extends JpaRepository<LocationCategory, Long> {

    Optional<LocationCategory> findByLocationIdAndCategoryId(Long locationId, int categoryId);
}
