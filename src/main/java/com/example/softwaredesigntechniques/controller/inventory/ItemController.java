package com.example.softwaredesigntechniques.controller.inventory;

import com.example.softwaredesigntechniques.domain.inventory.Item;
import com.example.softwaredesigntechniques.service.inventory.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/inventory/items")
@CrossOrigin(origins = "*")
public class ItemController {

    @Autowired
    private ItemService itemService;

    @GetMapping
    @PreAuthorize("hasRole('USER') or hasRole('INVENTORY_ADMIN')")
    public ResponseEntity<List<Item>> getAllItems() {
        return ResponseEntity.ok(itemService.getAll());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('INVENTORY_ADMIN')")
    public ResponseEntity<Item> getItem(@PathVariable String id) {
        UUID uuid = UUID.fromString(id);
        return ResponseEntity.ok(itemService.get(uuid));
    }

    @GetMapping("/sku/{sku}")
    @PreAuthorize("hasRole('USER') or hasRole('INVENTORY_ADMIN')")
    public ResponseEntity<Item> getItemBySku(@PathVariable String sku) {
        Optional<Item> item = itemService.findBySku(sku);
        return item.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/search")
    @PreAuthorize("hasRole('USER') or hasRole('INVENTORY_ADMIN')")
    public ResponseEntity<List<Item>> findItemsByName(@RequestParam String name) {
        return ResponseEntity.ok(itemService.findByNameContaining(name));
    }

    @GetMapping("/reorder-point/{reorderPoint}")
    @PreAuthorize("hasRole('USER') or hasRole('INVENTORY_ADMIN')")
    public ResponseEntity<List<Item>> getItemsByReorderPoint(@PathVariable Integer reorderPoint) {
        return ResponseEntity.ok(itemService.findByReorderPointGreaterThan(reorderPoint));
    }

    @PostMapping
    @PreAuthorize("hasRole('USER') or hasRole('INVENTORY_ADMIN')")
    public ResponseEntity<Item> createItem(@RequestBody Item item) {
        return ResponseEntity.ok(itemService.save(item));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('INVENTORY_ADMIN')")
    public ResponseEntity<Item> updateItem(@PathVariable String id, @RequestBody Item item) {
        UUID uuid = UUID.fromString(id);
        return ResponseEntity.ok(itemService.update(uuid, item));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('INVENTORY_ADMIN')")
    public ResponseEntity<Void> deleteItem(@PathVariable String id) {
        UUID uuid = UUID.fromString(id);
        itemService.delete(uuid);
        return ResponseEntity.ok().build();
    }

    // Admin operations
    @PostMapping("/admin/recreate-table")
    @PreAuthorize("hasRole('INVENTORY_ADMIN')")
    public ResponseEntity<String> recreateTable() {
        itemService.recreateTable();
        return ResponseEntity.ok("Item table recreated successfully");
    }

    @PostMapping("/admin/truncate-table")
    @PreAuthorize("hasRole('INVENTORY_ADMIN')")
    public ResponseEntity<String> truncateTable() {
        itemService.truncateTable();
        return ResponseEntity.ok("Item table truncated successfully");
    }

    @PostMapping("/admin/backup-data")
    @PreAuthorize("hasRole('INVENTORY_ADMIN')")
    public ResponseEntity<String> backupData() {
        itemService.backupData();
        return ResponseEntity.ok("Item data backup completed");
    }
}
