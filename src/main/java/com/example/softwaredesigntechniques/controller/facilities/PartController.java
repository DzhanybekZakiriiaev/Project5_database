package com.example.softwaredesigntechniques.controller.facilities;

import com.example.softwaredesigntechniques.domain.facilities.Part;
import com.example.softwaredesigntechniques.service.facilities.PartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/facilities/parts")
@CrossOrigin(origins = "*")
public class PartController {

    @Autowired
    private PartService partService;

    @GetMapping
    @PreAuthorize("hasRole('USER') or hasRole('FACILITIES_ADMIN')")
    public ResponseEntity<List<Part>> getAllParts() {
        return ResponseEntity.ok(partService.getAll());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('FACILITIES_ADMIN')")
    public ResponseEntity<Part> getPart(@PathVariable Long id) {
        return ResponseEntity.ok(partService.get(id));
    }

    @GetMapping("/search")
    @PreAuthorize("hasRole('USER') or hasRole('FACILITIES_ADMIN')")
    public ResponseEntity<List<Part>> findPartsByName(@RequestParam String name) {
        return ResponseEntity.ok(partService.findByNameContaining(name));
    }

    @GetMapping("/machine/{machineId}")
    @PreAuthorize("hasRole('USER') or hasRole('FACILITIES_ADMIN')")
    public ResponseEntity<List<Part>> getPartsByMachine(@PathVariable Long machineId) {
        return ResponseEntity.ok(partService.findByMachineId(machineId));
    }

    @PostMapping
    @PreAuthorize("hasRole('USER') or hasRole('FACILITIES_ADMIN')")
    public ResponseEntity<Part> createPart(@RequestBody Part part) {
        return ResponseEntity.ok(partService.save(part));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('FACILITIES_ADMIN')")
    public ResponseEntity<Part> updatePart(@PathVariable Long id, @RequestBody Part part) {
        return ResponseEntity.ok(partService.update(id, part));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('FACILITIES_ADMIN')")
    public ResponseEntity<Void> deletePart(@PathVariable Long id) {
        partService.delete(id);
        return ResponseEntity.ok().build();
    }

    // Admin operations
    @PostMapping("/admin/recreate-table")
    @PreAuthorize("hasRole('FACILITIES_ADMIN')")
    public ResponseEntity<String> recreateTable() {
        partService.recreateTable();
        return ResponseEntity.ok("Part table recreated successfully");
    }

    @PostMapping("/admin/truncate-table")
    @PreAuthorize("hasRole('FACILITIES_ADMIN')")
    public ResponseEntity<String> truncateTable() {
        partService.truncateTable();
        return ResponseEntity.ok("Part table truncated successfully");
    }

    @PostMapping("/admin/backup-data")
    @PreAuthorize("hasRole('FACILITIES_ADMIN')")
    public ResponseEntity<String> backupData() {
        partService.backupData();
        return ResponseEntity.ok("Part data backup completed");
    }
}
