package de.hoogvliet.socialservices.socialservice;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface LocationCategoryRepository extends JpaRepository<LocationCategory, Long> {
    Optional<LocationCategory> findByLocationIdAndCategoryId(Long locationId, int categoryId);

    @Query("SELECT lc.location from LocationCategory lc WHERE lc.category.id = :categoryId")
    List<Location> findLocationsByCategoryId(@Param("categoryId") Integer categoryId);

    List<LocationCategory> findByCityId(Long cityId);

    @Query("SELECT lc.location from LocationCategory lc WHERE lc.category.id = :categoryId and lc.city.id = :cityId")
    List<Location> findLocationsByCategoryIdAndCityId(Integer categoryId, Integer cityId);
}
