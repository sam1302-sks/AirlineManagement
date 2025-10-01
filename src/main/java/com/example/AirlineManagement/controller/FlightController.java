package com.example.AirlineManagement.controller;

import com.example.AirlineManagement.entity.*;
import com.example.AirlineManagement.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/flights")
public class FlightController {

    @Autowired
    private FlightRepository flightRepository;

    @GetMapping("/search")
    public ResponseEntity<?> searchFlight(@RequestParam int flightId) {
        return flightRepository.findByFlightId(flightId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
