package com.example.AirlineManagement.controller;

import com.example.AirlineManagement.entity.*;
import com.example.AirlineManagement.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    @Autowired
    private PassengerRepository passengerRepository;

    @Autowired
    private TicketRepository ticketRepository;

    // ==================== AIRLINES ENDPOINTS ====================

    @GetMapping("/airlines")
    public List<Airline> getAllAirlines() {
        return airlineRepository.findAll();
    }

    @GetMapping("/airlines/search")
    public List<Airline> searchAirlines(@RequestParam String query) {
        return airlineRepository.findByNameContaining(query);
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

    // ==================== TERMINALS ENDPOINTS ====================

    @GetMapping("/terminals")
    public List<Terminal> getAllTerminals() {
        return terminalRepository.findAll();
    }

    @GetMapping("/terminals/search")
    public List<Terminal> searchTerminals(@RequestParam String query) {
        return terminalRepository.findByNameContaining(query);
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

    // ==================== ROUTES ENDPOINTS ====================

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
            e.printStackTrace(); // This will help debug issues
            return ResponseEntity.badRequest().build();
        }
    }

    // ADD THIS - DELETE ROUTE ENDPOINT
    @DeleteMapping("/routes/{id}")
    public ResponseEntity<?> deleteRoute(@PathVariable Integer id) {
        try {
            if (routeRepository.existsById(id)) {
                routeRepository.deleteById(id);
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    // ==================== FLIGHTS ENDPOINTS ====================

    @GetMapping("/flights")
    public List<Flight> getAllFlights() {
        return flightRepository.findAll();
    }

    @GetMapping("/flights/search")
    public List<Flight> searchFlights(@RequestParam String query) {
        return flightRepository.findAll().stream()
                .filter(flight ->
                        flight.getStatus().toLowerCase().contains(query.toLowerCase()) ||
                                (flight.getRoute() != null &&
                                        (flight.getRoute().getOriginTerminal().getName().toLowerCase().contains(query.toLowerCase()) ||
                                                flight.getRoute().getDestinationTerminal().getName().toLowerCase().contains(query.toLowerCase())))
                )
                .collect(Collectors.toList());
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

    // ==================== AIRCRAFT (ZIP) ENDPOINTS ====================

    @GetMapping("/aircraft")
    public List<Zip> getAllAircraft() {
        return zipRepository.findAll();
    }

    @GetMapping("/aircraft/search")
    public List<Zip> searchAircraft(@RequestParam String query) {
        return zipRepository.findByModel(query);
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

    // ADD THIS - DELETE AIRCRAFT ENDPOINT
    @DeleteMapping("/aircraft/{id}")
    public ResponseEntity<?> deleteAircraft(@PathVariable Integer id) {
        try {
            if (zipRepository.existsById(id)) {
                zipRepository.deleteById(id);
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // ==================== PASSENGERS ENDPOINTS ====================

    @GetMapping("/passengers")
    public List<Passenger> getAllPassengers() {
        return passengerRepository.findAll();
    }

    @GetMapping("/passengers/search")
    public List<Passenger> searchPassengers(@RequestParam String query) {
        return passengerRepository.findByNameContaining(query);
    }

    @PostMapping("/passengers")
    public ResponseEntity<Passenger> createPassenger(@RequestBody Passenger passenger) {
        try {
            Passenger savedPassenger = passengerRepository.save(passenger);
            return ResponseEntity.ok(savedPassenger);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // ADD THIS - DELETE PASSENGER ENDPOINT
    @DeleteMapping("/passengers/{id}")
    public ResponseEntity<?> deletePassenger(@PathVariable Integer id) {
        try {
            if (passengerRepository.existsById(id)) {
                passengerRepository.deleteById(id);
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // ==================== TICKETS ENDPOINTS ====================

    @GetMapping("/tickets")
    public List<Ticket> getAllTickets() {
        return ticketRepository.findAll();
    }

    @GetMapping("/tickets/flight/{flightId}")
    public List<Ticket> getTicketsByFlight(@PathVariable Integer flightId) {
        return ticketRepository.findByFlightFlightId(flightId);
    }

    @PostMapping("/tickets")
    public ResponseEntity<Ticket> createTicket(@RequestBody Ticket ticket) {
        try {
            Ticket savedTicket = ticketRepository.save(ticket);
            return ResponseEntity.ok(savedTicket);
        } catch (Exception e) {
            e.printStackTrace(); // This will help debug issues
            return ResponseEntity.badRequest().build();
        }
    }

    // ADD THIS - DELETE TICKET ENDPOINT
    @DeleteMapping("/tickets/{id}")
    public ResponseEntity<?> deleteTicket(@PathVariable Integer id) {
        try {
            if (ticketRepository.existsById(id)) {
                ticketRepository.deleteById(id);
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }
}
