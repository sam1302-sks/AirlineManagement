package com.example.AirlineManagement.repository;

import com.example.AirlineManagement.entity.Route;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RouteRepository extends JpaRepository<Route, Integer> {

    List<Route> findByAirlineAirlineId(Integer airlineId);

    List<Route> findByOriginTerminalTerminalId(Integer terminalId);

    List<Route> findByDestinationTerminalTerminalId(Integer terminalId);

    @Query("SELECT r FROM Route r WHERE r.originTerminal.terminalId = ?1 AND r.destinationTerminal.terminalId = ?2")
    List<Route> findByOriginAndDestination(Integer originId, Integer destinationId);

    @Query("SELECT COUNT(r) FROM Route r")
    Long countAllRoutes();
}
