package com.example.softwaredesigntechniques.service.facilities;

import com.example.softwaredesigntechniques.domain.facilities.Part;
import com.example.softwaredesigntechniques.exception.NotFoundException;

import java.util.List;

public interface PartService {
    // CRUD operations available to all users
    Part get(Long id) throws NotFoundException;
    List<Part> getAll();
    List<Part> findByNameContaining(String name);
    List<Part> findByMachineId(Long machineId);
    Part save(Part part);
    Part update(Long id, Part part) throws NotFoundException;
    void delete(Long id) throws NotFoundException;

    // Admin operations for database altering
    void recreateTable();
    void truncateTable();
    void backupData();
}
