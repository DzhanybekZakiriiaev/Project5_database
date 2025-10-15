package com.example.softwaredesigntechniques.controller.inventory;

import com.example.softwaredesigntechniques.domain.inventory.StockLevel;
import com.example.softwaredesigntechniques.service.inventory.StockLevelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/inventory/stock-levels")
@CrossOrigin(origins = "*")
public class StockLevelController {

    @Autowired
    private StockLevelService stockLevelService;

    @GetMapping
    @PreAuthorize("hasRole('USER') or hasRole('INVENTORY_ADMIN')")
    public ResponseEntity<List<StockLevel>> getAllStockLevels() {
        return ResponseEntity.ok(stockLevelService.getAll());
    }

    @GetMapping("/{itemId}")
    @PreAuthorize("hasRole('USER') or hasRole('INVENTORY_ADMIN')")
    public ResponseEntity<StockLevel> getStockLevel(@PathVariable String itemId) {
        UUID uuid = UUID.fromString(itemId);
        return ResponseEntity.ok(stockLevelService.get(uuid));
    }

    @GetMapping("/below-level/{level}")
    @PreAuthorize("hasRole('USER') or hasRole('INVENTORY_ADMIN')")
    public ResponseEntity<List<StockLevel>> getStockLevelsBelowLevel(@PathVariable Integer level) {
        return ResponseEntity.ok(stockLevelService.findByLevelLessThan(level));
    }

    @PostMapping
    @PreAuthorize("hasRole('USER') or hasRole('INVENTORY_ADMIN')")
    public ResponseEntity<StockLevel> createStockLevel(@RequestBody StockLevel stockLevel) {
        return ResponseEntity.ok(stockLevelService.save(stockLevel));
    }

    @PutMapping("/{itemId}")
    @PreAuthorize("hasRole('USER') or hasRole('INVENTORY_ADMIN')")
    public ResponseEntity<StockLevel> updateStockLevel(@PathVariable String itemId, @RequestBody StockLevel stockLevel) {
        UUID uuid = UUID.fromString(itemId);
        return ResponseEntity.ok(stockLevelService.update(uuid, stockLevel));
    }

    @DeleteMapping("/{itemId}")
    @PreAuthorize("hasRole('USER') or hasRole('INVENTORY_ADMIN')")
    public ResponseEntity<Void> deleteStockLevel(@PathVariable String itemId) {
        UUID uuid = UUID.fromString(itemId);
        stockLevelService.delete(uuid);
        return ResponseEntity.ok().build();
    }

    // Admin operations
    @PostMapping("/admin/recreate-table")
    @PreAuthorize("hasRole('INVENTORY_ADMIN')")
    public ResponseEntity<String> recreateTable() {
        stockLevelService.recreateTable();
        return ResponseEntity.ok("StockLevel table recreated successfully");
    }

    @PostMapping("/admin/truncate-table")
    @PreAuthorize("hasRole('INVENTORY_ADMIN')")
    public ResponseEntity<String> truncateTable() {
        stockLevelService.truncateTable();
        return ResponseEntity.ok("StockLevel table truncated successfully");
    }

    @PostMapping("/admin/backup-data")
    @PreAuthorize("hasRole('INVENTORY_ADMIN')")
    public ResponseEntity<String> backupData() {
        stockLevelService.backupData();
        return ResponseEntity.ok("StockLevel data backup completed");
    }
}
