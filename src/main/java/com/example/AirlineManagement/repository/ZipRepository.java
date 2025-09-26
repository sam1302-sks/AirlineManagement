package com.example.AirlineManagement.repository;

import com.example.AirlineManagement.entity.Aircraft;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ZipRepository extends JpaRepository<Aircraft, Integer> {

    List<Aircraft> findByModel(String model);

    @Query("SELECT z FROM Aircraft z WHERE z.capacity >= ?1")
    List<Aircraft> findByMinCapacity(Integer minCapacity);

    @Query("SELECT COUNT(z) FROM Aircraft z")
    Long countAllZips();
}
