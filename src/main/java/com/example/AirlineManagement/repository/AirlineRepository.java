package com.example.AirlineManagement.repository;

import com.example.AirlineManagement.entity.Airline;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AirlineRepository extends JpaRepository<Airline, Integer> {

    @Query("SELECT a FROM Airline a WHERE a.name LIKE %?1%")
    List<Airline> findByNameContaining(String name);

    @Query("SELECT COUNT(a) FROM Airline a")
    Long countAllAirlines();
}
