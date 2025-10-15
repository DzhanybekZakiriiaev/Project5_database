package com.example.softwaredesigntechniques.controller.inventory;

import com.example.softwaredesigntechniques.domain.inventory.StockLedger;
import com.example.softwaredesigntechniques.service.inventory.StockLedgerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/inventory/stock-ledger")
@CrossOrigin(origins = "*")
public class StockLedgerController {

    @Autowired
    private StockLedgerService stockLedgerService;

    @GetMapping
    @PreAuthorize("hasRole('USER') or hasRole('INVENTORY_ADMIN')")
    public ResponseEntity<List<StockLedger>> getAllStockLedgers() {
        return ResponseEntity.ok(stockLedgerService.getAll());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('INVENTORY_ADMIN')")
    public ResponseEntity<StockLedger> getStockLedger(@PathVariable String id) {
        UUID uuid = UUID.fromString(id);
        return ResponseEntity.ok(stockLedgerService.get(uuid));
    }

    @GetMapping("/item/{itemId}")
    @PreAuthorize("hasRole('USER') or hasRole('INVENTORY_ADMIN')")
    public ResponseEntity<List<StockLedger>> getStockLedgersByItem(@PathVariable String itemId) {
        UUID uuid = UUID.fromString(itemId);
        return ResponseEntity.ok(stockLedgerService.findByItemId(uuid));
    }

    @GetMapping("/ref/{refId}")
    @PreAuthorize("hasRole('USER') or hasRole('INVENTORY_ADMIN')")
    public ResponseEntity<StockLedger> getStockLedgerByRefId(@PathVariable String refId) {
        Optional<StockLedger> stockLedger = stockLedgerService.findByRefId(refId);
        return stockLedger.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasRole('USER') or hasRole('INVENTORY_ADMIN')")
    public ResponseEntity<StockLedger> createStockLedger(@RequestBody StockLedger stockLedger) {
        return ResponseEntity.ok(stockLedgerService.save(stockLedger));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('INVENTORY_ADMIN')")
    public ResponseEntity<StockLedger> updateStockLedger(@PathVariable String id, @RequestBody StockLedger stockLedger) {
        UUID uuid = UUID.fromString(id);
        return ResponseEntity.ok(stockLedgerService.update(uuid, stockLedger));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('INVENTORY_ADMIN')")
    public ResponseEntity<Void> deleteStockLedger(@PathVariable String id) {
        UUID uuid = UUID.fromString(id);
        stockLedgerService.delete(uuid);
        return ResponseEntity.ok().build();
    }

    // Admin operations
    @PostMapping("/admin/recreate-table")
    @PreAuthorize("hasRole('INVENTORY_ADMIN')")
    public ResponseEntity<String> recreateTable() {
        stockLedgerService.recreateTable();
        return ResponseEntity.ok("StockLedger table recreated successfully");
    }

    @PostMapping("/admin/truncate-table")
    @PreAuthorize("hasRole('INVENTORY_ADMIN')")
    public ResponseEntity<String> truncateTable() {
        stockLedgerService.truncateTable();
        return ResponseEntity.ok("StockLedger table truncated successfully");
    }

    @PostMapping("/admin/backup-data")
    @PreAuthorize("hasRole('INVENTORY_ADMIN')")
    public ResponseEntity<String> backupData() {
        stockLedgerService.backupData();
        return ResponseEntity.ok("StockLedger data backup completed");
    }
}
