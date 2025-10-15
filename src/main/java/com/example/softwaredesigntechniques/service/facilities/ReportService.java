package com.example.softwaredesigntechniques.service.facilities;

import com.example.softwaredesigntechniques.domain.facilities.Report;
import com.example.softwaredesigntechniques.exception.NotFoundException;

import java.util.List;

public interface ReportService {
    // CRUD operations available to all users
    Report get(Long id) throws NotFoundException;
    List<Report> getAll();
    List<Report> findByNeedsRepair(Boolean needsRepair);
    List<Report> findByMachineId(Long machineId);
    List<Report> findByPartId(Long partId);
    List<Report> findByLogId(Long logId);
    Report save(Report report);
    Report update(Long id, Report report) throws NotFoundException;
    void delete(Long id) throws NotFoundException;

    // Admin operations for database altering
    void recreateTable();
    void truncateTable();
    void backupData();
}
