package com.example.AirlineManagement.controller;

import com.example.AirlineManagement.entity.*;
import com.example.AirlineManagement.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/crew")
public class CrewController {

    @Autowired
    private CrewRepository crewRepository;

    @GetMapping
    public List<Crew> getAllCrew() {
        return crewRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Crew> getCrewById(@PathVariable Long id) {
        return crewRepository.findById(Math.toIntExact(id))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Crew addCrew(@RequestBody Crew crew) {
        return crewRepository.save(crew);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Crew> updateCrew(@PathVariable Long id, @RequestBody Crew crewDetails) {
        return crewRepository.findById(Math.toIntExact(id)).map(crew -> {
            crew.setFirstName(crewDetails.getFirstName());
            crew.setRole(crewDetails.getRole());
            crew.setAssignments(crewDetails.getAssignments());
            return ResponseEntity.ok(crewRepository.save(crew));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCrew(@PathVariable Long id) {
        return crewRepository.findById(Math.toIntExact(id)).map(crew -> {
            crewRepository.delete(crew);
            return ResponseEntity.ok().<Void>build();
        }).orElse(ResponseEntity.notFound().build());
    }
}
