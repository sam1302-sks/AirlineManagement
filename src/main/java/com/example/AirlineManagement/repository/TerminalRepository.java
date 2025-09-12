package com.example.AirlineManagement.repository;

import com.example.AirlineManagement.entity.Terminal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TerminalRepository extends JpaRepository<Terminal, Integer> {

    List<Terminal> findByCity(String city);

    List<Terminal> findByCountry(String country);

    @Query("SELECT t FROM Terminal t WHERE t.name LIKE %?1%")
    List<Terminal> findByNameContaining(String name);

    @Query("SELECT COUNT(t) FROM Terminal t")
    Long countAllTerminals();
}
