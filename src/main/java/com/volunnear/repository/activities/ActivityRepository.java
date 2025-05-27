package com.volunnear.repository.activities;

import com.volunnear.entity.activities.Activity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActivityRepository extends JpaRepository<Activity, Integer> {
    List<Activity> findAllActivitiesByKindOfActivityIgnoreCaseIn(List<String> preferences);

    boolean deleteAllById(Integer id);

    @Query(value = """
    SELECT a.*, 
      ST_Distance(
        a.location::geography,
        ST_SetSRID(ST_MakePoint(:lon, :lat), 4326)::geography
      ) AS distance
    FROM activity a
    WHERE ST_DWithin(
      a.location::geography,
      ST_SetSRID(ST_MakePoint(:lon, :lat), 4326)::geography,
      :radius
    )
    ORDER BY distance ASC
    """, nativeQuery = true)
    List<Object[]> findNearbyActivities(
            @Param("lon") double lon,
            @Param("lat") double lat,
            @Param("radius") double radiusInMeters);
}