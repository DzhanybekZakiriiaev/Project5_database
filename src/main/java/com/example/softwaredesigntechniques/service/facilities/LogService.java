package com.example.softwaredesigntechniques.service.facilities;

import com.example.softwaredesigntechniques.domain.facilities.Log;
import com.example.softwaredesigntechniques.exception.NotFoundException;

import java.time.LocalDate;
import java.util.List;

public interface LogService {
    // CRUD operations available to all users
    Log get(Long id) throws NotFoundException;
    List<Log> getAll();
    List<Log> findByDateBetween(LocalDate startDate, LocalDate endDate);
    Log save(Log log);
    Log update(Long id, Log log) throws NotFoundException;
    void delete(Long id) throws NotFoundException;

    // Admin operations for database altering
    void recreateTable();
    void truncateTable();
    void backupData();
}
