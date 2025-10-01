package com.example.AirlineManagement.controller;

import com.example.AirlineManagement.entity.*;
import com.example.AirlineManagement.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/passengers")
public class PassengerController {

    @Autowired
    private PassengerRepository passengerRepository;

    @GetMapping("/search")
    public List<Passenger> searchPassenger(@RequestParam String name) {
        return passengerRepository.findByFirstNameContainingIgnoreCase(name);
    }
}
