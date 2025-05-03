package de.hoogvliet.socialservices.socialservice;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface LocationCityRepository extends JpaRepository<LocationCity, Long> {
    Optional<LocationCity> findByLocationIdAndCityId(Long locationId, Long cityId);

    @Query("SELECT lc.location from LocationCity lc WHERE lc.city.id = :cityId")
    List<Location> findLocationsByCityId(@Param("cityId") Integer cityId);
}