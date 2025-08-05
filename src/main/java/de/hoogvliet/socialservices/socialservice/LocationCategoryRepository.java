package de.hoogvliet.socialservices.socialservice;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface LocationCategoryRepository extends JpaRepository<LocationCategory, Long> {
    Optional<LocationCategory> findByLocationIdAndCategoryId(Long locationId, int categoryId);

    @Query("SELECT lc.location from LocationCategory lc WHERE lc.category.id = :categoryId")
    List<Location> findLocationsByCategoryId(@Param("categoryId") Integer categoryId);

    List<LocationCategory> findByCityId(Long cityId);

    @Query("SELECT lc.location from LocationCategory lc WHERE lc.category.id = :categoryId and lc.city.id = :cityId")
    List<Location> findLocationsByCategoryIdAndCityId(Integer categoryId, Integer cityId);

    @Query("SELECT lc.category from LocationCategory lc WHERE lc.city.id = :cityId GROUP BY lc.category")
    List<Category> findLocationCategoriesByCity(int cityId);

    @Modifying
    @Transactional
    @Query(value = """
DELETE FROM location_category WHERE id in (
    select A.id from location_category A LEFT JOIN location B ON A.location_id = B.id WHERE B.id is null)
    """, nativeQuery = true)
    void deleteOrphanedLocationMappings();

    @Query("SELECT lc.category from LocationCategory lc WHERE lc.location.id = :locationId")
    List<Category> findCategoriesByLocationId(long locationId);

    @Modifying
    @Transactional
    @Query("DELETE FROM LocationCategory lc WHERE lc.location.id = :locationId AND lc.category.id = :categoryId")
    void deleteByCategoryIdAndLocationId(int categoryId, long locationId);
}