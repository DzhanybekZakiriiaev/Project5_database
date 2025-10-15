package com.example.softwaredesigntechniques.repository.facilities;

import com.example.softwaredesigntechniques.domain.facilities.Part;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PartRepository extends JpaRepository<Part, Long> {
    List<Part> findByNameContainingIgnoreCase(String name);
    List<Part> findByMachineId(Long machineId);
}
