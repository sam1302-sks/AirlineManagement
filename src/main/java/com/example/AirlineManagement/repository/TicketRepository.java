package com.example.AirlineManagement.repository;

import com.example.AirlineManagement.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Integer> {

    // Find tickets by flight ID
    List<Ticket> findByFlightFlightId(Integer flightId);

    // Find tickets by passenger ID
    List<Ticket> findByPassengerPassengerId(Integer passengerId);

    // Get total revenue from all tickets
    @Query("SELECT SUM(t.price) FROM Ticket t")
    Double getTotalRevenue();

    // Get revenue by airline (for dashboard statistics)
    @Query("SELECT a.name, SUM(t.price) FROM Ticket t " +
            "JOIN t.flight f JOIN f.route r JOIN r.airline a " +
            "GROUP BY a.airlineId, a.name ORDER BY SUM(t.price) DESC")
    List<Object[]> getRevenueByAirline();

    // Get ticket count by flight
    @Query("SELECT f.flightId, COUNT(t) FROM Ticket t " +
            "JOIN t.flight f " +
            "GROUP BY f.flightId")
    List<Object[]> getTicketCountByFlight();

    // Get revenue by route
    @Query("SELECT CONCAT(ot.name, ' -> ', dt.name), SUM(t.price) FROM Ticket t " +
            "JOIN t.flight f JOIN f.route r " +
            "JOIN r.originTerminal ot JOIN r.destinationTerminal dt " +
            "GROUP BY r.routeId ORDER BY SUM(t.price) DESC")
    List<Object[]> getRevenueByRoute();

    // Get tickets by seat number
    List<Ticket> findBySeatNumber(String seatNumber);

    // Count total tickets
    @Query("SELECT COUNT(t) FROM Ticket t")
    Long countAllTickets();

    // Get average ticket price
    @Query("SELECT AVG(t.price) FROM Ticket t")
    Double getAverageTicketPrice();

    // Get tickets with price greater than specified amount
    @Query("SELECT t FROM Ticket t WHERE t.price > ?1")
    List<Ticket> findTicketsWithPriceGreaterThan(Double price);

    // Get most expensive tickets
    @Query("SELECT t FROM Ticket t ORDER BY t.price DESC")
    List<Ticket> findMostExpensiveTickets();
}
