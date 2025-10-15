package com.example.softwaredesigntechniques.service.facilities;

import com.example.softwaredesigntechniques.domain.facilities.Machine;
import com.example.softwaredesigntechniques.exception.NotFoundException;

import java.util.List;

public interface MachineService {
    // CRUD operations available to all users
    Machine get(Long id) throws NotFoundException;
    List<Machine> getAll();
    List<Machine> findByNameContaining(String name);
    Machine save(Machine machine);
    Machine update(Long id, Machine machine) throws NotFoundException;
    void delete(Long id) throws NotFoundException;

    // Admin operations for database altering
    void recreateTable();
    void truncateTable();
    void backupData();
}
