package com.example.softwaredesigntechniques.controller.facilities;

import com.example.softwaredesigntechniques.domain.facilities.Report;
import com.example.softwaredesigntechniques.service.facilities.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/facilities/reports")
@CrossOrigin(origins = "*")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @GetMapping
    @PreAuthorize("hasRole('USER') or hasRole('FACILITIES_ADMIN')")
    public ResponseEntity<List<Report>> getAllReports() {
        return ResponseEntity.ok(reportService.getAll());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('FACILITIES_ADMIN')")
    public ResponseEntity<Report> getReport(@PathVariable Long id) {
        return ResponseEntity.ok(reportService.get(id));
    }

    @GetMapping("/needs-repair/{needsRepair}")
    @PreAuthorize("hasRole('USER') or hasRole('FACILITIES_ADMIN')")
    public ResponseEntity<List<Report>> getReportsByNeedsRepair(@PathVariable Boolean needsRepair) {
        return ResponseEntity.ok(reportService.findByNeedsRepair(needsRepair));
    }

    @GetMapping("/machine/{machineId}")
    @PreAuthorize("hasRole('USER') or hasRole('FACILITIES_ADMIN')")
    public ResponseEntity<List<Report>> getReportsByMachine(@PathVariable Long machineId) {
        return ResponseEntity.ok(reportService.findByMachineId(machineId));
    }

    @GetMapping("/part/{partId}")
    @PreAuthorize("hasRole('USER') or hasRole('FACILITIES_ADMIN')")
    public ResponseEntity<List<Report>> getReportsByPart(@PathVariable Long partId) {
        return ResponseEntity.ok(reportService.findByPartId(partId));
    }

    @GetMapping("/log/{logId}")
    @PreAuthorize("hasRole('USER') or hasRole('FACILITIES_ADMIN')")
    public ResponseEntity<List<Report>> getReportsByLog(@PathVariable Long logId) {
        return ResponseEntity.ok(reportService.findByLogId(logId));
    }

    @PostMapping
    @PreAuthorize("hasRole('USER') or hasRole('FACILITIES_ADMIN')")
    public ResponseEntity<Report> createReport(@RequestBody Report report) {
        return ResponseEntity.ok(reportService.save(report));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('FACILITIES_ADMIN')")
    public ResponseEntity<Report> updateReport(@PathVariable Long id, @RequestBody Report report) {
        return ResponseEntity.ok(reportService.update(id, report));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('FACILITIES_ADMIN')")
    public ResponseEntity<Void> deleteReport(@PathVariable Long id) {
        reportService.delete(id);
        return ResponseEntity.ok().build();
    }

    // Admin operations
    @PostMapping("/admin/recreate-table")
    @PreAuthorize("hasRole('FACILITIES_ADMIN')")
    public ResponseEntity<String> recreateTable() {
        reportService.recreateTable();
        return ResponseEntity.ok("Report table recreated successfully");
    }

    @PostMapping("/admin/truncate-table")
    @PreAuthorize("hasRole('FACILITIES_ADMIN')")
    public ResponseEntity<String> truncateTable() {
        reportService.truncateTable();
        return ResponseEntity.ok("Report table truncated successfully");
    }

    @PostMapping("/admin/backup-data")
    @PreAuthorize("hasRole('FACILITIES_ADMIN')")
    public ResponseEntity<String> backupData() {
        reportService.backupData();
        return ResponseEntity.ok("Report data backup completed");
    }
}
