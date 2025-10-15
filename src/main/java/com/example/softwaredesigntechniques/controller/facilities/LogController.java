package com.example.softwaredesigntechniques.controller.facilities;

import com.example.softwaredesigntechniques.domain.facilities.Log;
import com.example.softwaredesigntechniques.service.facilities.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/facilities/logs")
@CrossOrigin(origins = "*")
public class LogController {

    @Autowired
    private LogService logService;

    @GetMapping
    @PreAuthorize("hasRole('USER') or hasRole('FACILITIES_ADMIN')")
    public ResponseEntity<List<Log>> getAllLogs() {
        return ResponseEntity.ok(logService.getAll());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('FACILITIES_ADMIN')")
    public ResponseEntity<Log> getLog(@PathVariable Long id) {
        return ResponseEntity.ok(logService.get(id));
    }

    @GetMapping("/date-range")
    @PreAuthorize("hasRole('USER') or hasRole('FACILITIES_ADMIN')")
    public ResponseEntity<List<Log>> getLogsByDateRange(@RequestParam String startDate, @RequestParam String endDate) {
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);
        return ResponseEntity.ok(logService.findByDateBetween(start, end));
    }

    @PostMapping
    @PreAuthorize("hasRole('USER') or hasRole('FACILITIES_ADMIN')")
    public ResponseEntity<Log> createLog(@RequestBody Log log) {
        return ResponseEntity.ok(logService.save(log));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('FACILITIES_ADMIN')")
    public ResponseEntity<Log> updateLog(@PathVariable Long id, @RequestBody Log log) {
        return ResponseEntity.ok(logService.update(id, log));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('FACILITIES_ADMIN')")
    public ResponseEntity<Void> deleteLog(@PathVariable Long id) {
        logService.delete(id);
        return ResponseEntity.ok().build();
    }

    // Admin operations
    @PostMapping("/admin/recreate-table")
    @PreAuthorize("hasRole('FACILITIES_ADMIN')")
    public ResponseEntity<String> recreateTable() {
        logService.recreateTable();
        return ResponseEntity.ok("Log table recreated successfully");
    }

    @PostMapping("/admin/truncate-table")
    @PreAuthorize("hasRole('FACILITIES_ADMIN')")
    public ResponseEntity<String> truncateTable() {
        logService.truncateTable();
        return ResponseEntity.ok("Log table truncated successfully");
    }

    @PostMapping("/admin/backup-data")
    @PreAuthorize("hasRole('FACILITIES_ADMIN')")
    public ResponseEntity<String> backupData() {
        logService.backupData();
        return ResponseEntity.ok("Log data backup completed");
    }
}
