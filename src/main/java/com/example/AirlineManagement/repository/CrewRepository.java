package com.example.AirlineManagement.repository;

import com.example.AirlineManagement.entity.Crew;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CrewRepository extends JpaRepository<Crew, Integer> {

    List<Crew> findByRole(String role);

    @Query("SELECT c FROM Crew c WHERE c.firstName LIKE %?1% OR c.lastName LIKE %?1%")
    List<Crew> findByNameContaining(String name);

    @Query("SELECT COUNT(c) FROM Crew c")
    Long countAllCrew();
}
