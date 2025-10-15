package com.example.softwaredesigntechniques.service.facilities.impl;

import com.example.softwaredesigntechniques.domain.facilities.Report;
import com.example.softwaredesigntechniques.exception.NotFoundException;
import com.example.softwaredesigntechniques.repository.facilities.ReportRepository;
import com.example.softwaredesigntechniques.service.facilities.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ReportServiceImpl implements ReportService {

    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    @Transactional(readOnly = true)
    public Report get(Long id) throws NotFoundException {
        return reportRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Report not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Report> getAll() {
        return reportRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Report> findByNeedsRepair(Boolean needsRepair) {
        return reportRepository.findByNeedsRepair(needsRepair);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Report> findByMachineId(Long machineId) {
        return reportRepository.findByMachineId(machineId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Report> findByPartId(Long partId) {
        return reportRepository.findByPartId(partId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Report> findByLogId(Long logId) {
        return reportRepository.findByLogId(logId);
    }

    @Override
    public Report save(Report report) {
        return reportRepository.save(report);
    }

    @Override
    public Report update(Long id, Report report) throws NotFoundException {
        Report existingReport = get(id);
        existingReport.setReportText(report.getReportText());
        existingReport.setLog(report.getLog());
        existingReport.setPart(report.getPart());
        existingReport.setMachine(report.getMachine());
        existingReport.setNeedsRepair(report.getNeedsRepair());
        return reportRepository.save(existingReport);
    }

    @Override
    public void delete(Long id) throws NotFoundException {
        Report report = get(id);
        reportRepository.delete(report);
    }

    // Admin operations
    @Override
    public void recreateTable() {
        jdbcTemplate.execute("DROP TABLE IF EXISTS \"Reports\" CASCADE");
        jdbcTemplate.execute("CREATE TABLE \"Reports\"(" +
                "\"id\" BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY," +
                "\"report_text\" VARCHAR(255) NOT NULL," +
                "\"log_id\" BIGINT NOT NULL," +
                "\"part_id\" BIGINT NULL," +
                "\"machine_id\" BIGINT NULL," +
                "\"needs_repair\" BOOLEAN NOT NULL" +
                ")");
    }

    @Override
    public void truncateTable() {
        jdbcTemplate.execute("TRUNCATE TABLE \"Reports\" RESTART IDENTITY CASCADE");
    }

    @Override
    public void backupData() {
        System.out.println("Report data backup completed at: " + System.currentTimeMillis());
    }
}
