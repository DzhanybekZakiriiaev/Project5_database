package com.example.softwaredesigntechniques.controller.facilities;

import com.example.softwaredesigntechniques.domain.facilities.Machine;
import com.example.softwaredesigntechniques.service.facilities.MachineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/facilities/machines")
@CrossOrigin(origins = "*")
public class MachineController {

    @Autowired
    private MachineService machineService;

    @GetMapping
    @PreAuthorize("hasRole('USER') or hasRole('FACILITIES_ADMIN')")
    public ResponseEntity<List<Machine>> getAllMachines() {
        return ResponseEntity.ok(machineService.getAll());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('FACILITIES_ADMIN')")
    public ResponseEntity<Machine> getMachine(@PathVariable Long id) {
        return ResponseEntity.ok(machineService.get(id));
    }

    @GetMapping("/search")
    @PreAuthorize("hasRole('USER') or hasRole('FACILITIES_ADMIN')")
    public ResponseEntity<List<Machine>> findMachinesByName(@RequestParam String name) {
        return ResponseEntity.ok(machineService.findByNameContaining(name));
    }

    @PostMapping
    @PreAuthorize("hasRole('USER') or hasRole('FACILITIES_ADMIN')")
    public ResponseEntity<Machine> createMachine(@RequestBody Machine machine) {
        return ResponseEntity.ok(machineService.save(machine));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('FACILITIES_ADMIN')")
    public ResponseEntity<Machine> updateMachine(@PathVariable Long id, @RequestBody Machine machine) {
        return ResponseEntity.ok(machineService.update(id, machine));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('FACILITIES_ADMIN')")
    public ResponseEntity<Void> deleteMachine(@PathVariable Long id) {
        machineService.delete(id);
        return ResponseEntity.ok().build();
    }

    // Admin operations
    @PostMapping("/admin/recreate-table")
    @PreAuthorize("hasRole('FACILITIES_ADMIN')")
    public ResponseEntity<String> recreateTable() {
        machineService.recreateTable();
        return ResponseEntity.ok("Machine table recreated successfully");
    }

    @PostMapping("/admin/truncate-table")
    @PreAuthorize("hasRole('FACILITIES_ADMIN')")
    public ResponseEntity<String> truncateTable() {
        machineService.truncateTable();
        return ResponseEntity.ok("Machine table truncated successfully");
    }

    @PostMapping("/admin/backup-data")
    @PreAuthorize("hasRole('FACILITIES_ADMIN')")
    public ResponseEntity<String> backupData() {
        machineService.backupData();
        return ResponseEntity.ok("Machine data backup completed");
    }
}
