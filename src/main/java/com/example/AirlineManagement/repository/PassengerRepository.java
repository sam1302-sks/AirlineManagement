package com.example.AirlineManagement.repository;

import com.example.AirlineManagement.entity.Passenger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PassengerRepository extends JpaRepository<Passenger, Integer> {

    @Query("SELECT p FROM Passenger p WHERE p.firstName LIKE %?1% OR p.lastName LIKE %?1%")
    List<Passenger> findByNameContaining(String name);

    @Query("SELECT COUNT(p) FROM Passenger p")
    Long countAllPassengers();

    List<Passenger> findByFirstNameContainingIgnoreCase(String firstName);
    List<Passenger> findByLastNameContainingIgnoreCase(String lastName);
}
