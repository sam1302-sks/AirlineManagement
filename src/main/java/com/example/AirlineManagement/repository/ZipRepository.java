package com.example.AirlineManagement.repository;

import com.example.AirlineManagement.entity.Zip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ZipRepository extends JpaRepository<Zip, Integer> {

    List<Zip> findByModel(String model);

    @Query("SELECT z FROM Zip z WHERE z.capacity >= ?1")
    List<Zip> findByMinCapacity(Integer minCapacity);

    @Query("SELECT COUNT(z) FROM Zip z")
    Long countAllZips();
}
