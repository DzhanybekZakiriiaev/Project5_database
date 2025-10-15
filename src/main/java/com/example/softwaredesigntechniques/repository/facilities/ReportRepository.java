package com.example.softwaredesigntechniques.repository.facilities;

import com.example.softwaredesigntechniques.domain.facilities.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {
    List<Report> findByNeedsRepair(Boolean needsRepair);
    List<Report> findByMachineId(Long machineId);
    List<Report> findByPartId(Long partId);
    List<Report> findByLogId(Long logId);
}
