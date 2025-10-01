package com.example.AirlineManagement.repository;

import com.example.AirlineManagement.entity.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface FlightRepository extends JpaRepository<Flight, Integer> {

    List<Flight> findByStatus(String status);

    List<Flight> findByRouteRouteId(Integer routeId);

    List<Flight> findByAircraftAircraftId(Integer aircraftId);

    @Query("SELECT f FROM Flight f WHERE f.departureTime BETWEEN ?1 AND ?2")
    List<Flight> findByDepartureTimeBetween(LocalDateTime start, LocalDateTime end);

    @Query("SELECT COUNT(f) FROM Flight f")
    Long countAllFlights();

    @Query("SELECT f.status, COUNT(f) FROM Flight f GROUP BY f.status")
    List<Object[]> getFlightStatusDistribution();

    Optional<Flight> findByFlightId(Integer flightId);
}
