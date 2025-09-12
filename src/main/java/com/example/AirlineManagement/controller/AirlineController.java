package com.example.AirlineManagement.controller;

import com.example.AirlineManagement.entity.*;
import com.example.AirlineManagement.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class AirlineController {

    @Autowired
    private AirlineRepository airlineRepository;

    @Autowired
    private TerminalRepository terminalRepository;

    @Autowired
    private FlightRepository flightRepository;

    @Autowired
    private RouteRepository routeRepository;

    @Autowired
    private ZipRepository zipRepository;

    // Airlines endpoints
    @GetMapping("/airlines")
    public List<Airline> getAllAirlines() {
        return airlineRepository.findAll();
    }

    @PostMapping("/airlines")
    public ResponseEntity<Airline> createAirline(@RequestBody Airline airline) {
        try {
            Airline savedAirline = airlineRepository.save(airline);
            return ResponseEntity.ok(savedAirline);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/airlines/{id}")
    public ResponseEntity<?> deleteAirline(@PathVariable Integer id) {
        try {
            if (airlineRepository.existsById(id)) {
                airlineRepository.deleteById(id);
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Terminals endpoints
    @GetMapping("/terminals")
    public List<Terminal> getAllTerminals() {
        return terminalRepository.findAll();
    }

    @PostMapping("/terminals")
    public ResponseEntity<Terminal> createTerminal(@RequestBody Terminal terminal) {
        try {
            Terminal savedTerminal = terminalRepository.save(terminal);
            return ResponseEntity.ok(savedTerminal);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/terminals/{id}")
    public ResponseEntity<?> deleteTerminal(@PathVariable Integer id) {
        try {
            if (terminalRepository.existsById(id)) {
                terminalRepository.deleteById(id);
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Routes endpoints
    @GetMapping("/routes")
    public List<Route> getAllRoutes() {
        return routeRepository.findAll();
    }

    @PostMapping("/routes")
    public ResponseEntity<Route> createRoute(@RequestBody Route route) {
        try {
            Route savedRoute = routeRepository.save(route);
            return ResponseEntity.ok(savedRoute);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Flights endpoints
    @GetMapping("/flights")
    public List<Flight> getAllFlights() {
        return flightRepository.findAll();
    }

    @PostMapping("/flights")
    public ResponseEntity<Flight> createFlight(@RequestBody Flight flight) {
        try {
            Flight savedFlight = flightRepository.save(flight);
            return ResponseEntity.ok(savedFlight);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/flights/{id}/status")
    public ResponseEntity<Flight> updateFlightStatus(
            @PathVariable Integer id,
            @RequestParam String status) {
        Optional<Flight> flightOpt = flightRepository.findById(id);
        if (flightOpt.isPresent()) {
            Flight flight = flightOpt.get();
            flight.setStatus(status);
            Flight updatedFlight = flightRepository.save(flight);
            return ResponseEntity.ok(updatedFlight);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Aircraft (Zip) endpoints
    @GetMapping("/aircraft")
    public List<Zip> getAllAircraft() {
        return zipRepository.findAll();
    }

    @PostMapping("/aircraft")
    public ResponseEntity<Zip> createAircraft(@RequestBody Zip aircraft) {
        try {
            Zip savedAircraft = zipRepository.save(aircraft);
            return ResponseEntity.ok(savedAircraft);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
