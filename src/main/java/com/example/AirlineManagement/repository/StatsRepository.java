package com.example.AirlineManagement.repository;

import com.example.AirlineManagement.entity.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface StatsRepository extends JpaRepository<Flight, Integer> {

    @Query("SELECT COUNT(DISTINCT a) FROM Airline a")
    Long getTotalAirlines();

    @Query("SELECT COUNT(DISTINCT t) FROM Terminal t")
    Long getTotalTerminals();

    @Query("SELECT COUNT(DISTINCT r) FROM Route r")
    Long getTotalRoutes();

    @Query("SELECT COUNT(DISTINCT p) FROM Passenger p")
    Long getTotalPassengers();

    @Query("SELECT COUNT(DISTINCT ac) FROM Aircraft ac")
    Long getTotalAircraft();

    @Query("SELECT COUNT(DISTINCT c) FROM Crew c")
    Long getTotalCrew();

    @Query("SELECT COUNT(f) FROM Flight f")
    Long getTotalFlights();

    @Query("SELECT f.status, COUNT(f) FROM Flight f GROUP BY f.status")
    List<Object[]> getFlightStatusDistribution();

    @Query("SELECT a.name, SUM(t.price) FROM Ticket t " +
            "JOIN t.flight f JOIN f.route r JOIN r.airline a " +
            "GROUP BY a.airlineId ORDER BY SUM(t.price) DESC")
    List<Object[]> getRevenueByAirline();

    @Query("SELECT CONCAT(ot.name, ' -> ', dt.name), COUNT(f) " +
            "FROM Flight f JOIN f.route r " +
            "JOIN r.originTerminal ot JOIN r.destinationTerminal dt " +
            "GROUP BY r.routeId ORDER BY COUNT(f) DESC LIMIT 10")
    List<Object[]> getBusiestRoutes();
}
