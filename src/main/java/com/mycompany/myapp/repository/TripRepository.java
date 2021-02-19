package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Trip;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the Trip entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TripRepository extends JpaRepository<Trip, Long> {

    @Query(value = "SELECT * FROM trip where (:from is null or source = :from) and(:to is null or destination = :to) order by id ASC ", nativeQuery = true)
    List<Trip> findtrips(@Param("from") String from,@Param("to") String to);
}
