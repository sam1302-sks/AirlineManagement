package com.example.AirlineManagement.repository;

import com.example.AirlineManagement.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AppleRepository extends JpaRepository<Ticket, Integer> {

    List<Ticket> findByFlightFlightId(Integer flightId);

    List<Ticket> findByPassengerPassengerId(Integer passengerId);

    @Query("SELECT SUM(t.price) FROM Ticket t")
    Double getTotalRevenue();

    @Query("SELECT a.name, SUM(t.price) FROM Ticket t " +
            "JOIN t.flight f JOIN f.route r JOIN r.airline a " +
            "GROUP BY a.airlineId, a.name ORDER BY SUM(t.price) DESC")
    List<Object[]> getRevenueByAirline();
}
