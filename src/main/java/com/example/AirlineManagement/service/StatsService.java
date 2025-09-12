package com.example.AirlineManagement.service;

import com.example.AirlineManagement.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class StatsService {

    @Autowired
    private AirlineRepository airlineRepository;

    @Autowired
    private TerminalRepository terminalRepository;

    @Autowired
    private RouteRepository routeRepository;

    @Autowired
    private FlightRepository flightRepository;

    @Autowired
    private PassengerRepository passengerRepository;

    @Autowired
    private ZipRepository zipRepository;

    @Autowired
    private CrewRepository crewRepository;

    @Autowired
    private AppleRepository ticketRepository;

    public Map<String, Object> getDashboardStats() {
        Map<String, Object> stats = new HashMap<>();

        try {
            // Basic counts
            stats.put("totalAirlines", airlineRepository.count());
            stats.put("totalTerminals", terminalRepository.count());
            stats.put("totalRoutes", routeRepository.count());
            stats.put("totalPassengers", passengerRepository.count());
            stats.put("totalAircraft", zipRepository.count());
            stats.put("totalCrew", crewRepository.count());
            stats.put("totalFlights", flightRepository.count());

            // Flight status distribution
            List<Object[]> statusData = flightRepository.getFlightStatusDistribution();
            Map<String, Long> statusMap = statusData.stream()
                    .collect(Collectors.toMap(
                            row -> (String) row[0],
                            row -> (Long) row[1]
                    ));
            stats.put("flightStatusDistribution", statusMap);

            // Revenue by airline
            List<Object[]> revenueData = ticketRepository.getRevenueByAirline();
            List<Map<String, Object>> revenueList = revenueData.stream()
                    .map(row -> {
                        Map<String, Object> item = new HashMap<>();
                        item.put("airline", row[0]);
                        item.put("revenue", row[1]);
                        return item;
                    })
                    .collect(Collectors.toList());
            stats.put("revenueByAirline", revenueList);

            // Mock busiest routes
            stats.put("busiestRoutes", getMockBusiestRoutes());

        } catch (Exception e) {
            // Return default values if database is empty
            stats.put("totalAirlines", 0L);
            stats.put("totalTerminals", 0L);
            stats.put("totalRoutes", 0L);
            stats.put("totalPassengers", 0L);
            stats.put("totalAircraft", 0L);
            stats.put("totalCrew", 0L);
            stats.put("totalFlights", 0L);
            stats.put("flightStatusDistribution", new HashMap<>());
            stats.put("revenueByAirline", List.of());
            stats.put("busiestRoutes", List.of());
        }

        return stats;
    }

    private List<Map<String, Object>> getMockBusiestRoutes() {
        return List.of(
                Map.of("route", "Delhi -> Mumbai", "flights", 25),
                Map.of("route", "Mumbai -> Bangalore", "flights", 20),
                Map.of("route", "Delhi -> Bangalore", "flights", 18),
                Map.of("route", "Mumbai -> Chennai", "flights", 15)
        );
    }
}
