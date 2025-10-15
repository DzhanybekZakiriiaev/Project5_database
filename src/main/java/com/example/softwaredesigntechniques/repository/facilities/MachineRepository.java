package com.example.softwaredesigntechniques.repository.facilities;

import com.example.softwaredesigntechniques.domain.facilities.Machine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MachineRepository extends JpaRepository<Machine, Long> {
    List<Machine> findByNameContainingIgnoreCase(String name);
}
