package de.hoogvliet.socialservices.socialservice;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface CityRepository  extends JpaRepository<City, Long> {

    Optional<City> findByName(String name);

    @Modifying
    @Transactional
    @Query(value = """
DELETE FROM city WHERE id in (
    select c.id from city c LEFT JOIN location_category lc ON c.id = lc.city_id WHERE lc.id is null)
    """, nativeQuery = true)
    void deleteOrphanedCities();
}
