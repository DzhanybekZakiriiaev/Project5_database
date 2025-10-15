package com.example.softwaredesigntechniques.service.inventory;

import com.example.softwaredesigntechniques.domain.inventory.Item;
import com.example.softwaredesigntechniques.exception.NotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ItemService {
    // CRUD operations available to all users
    Item get(UUID id) throws NotFoundException;
    List<Item> getAll();
    Optional<Item> findBySku(String sku);
    List<Item> findByNameContaining(String name);
    List<Item> findByReorderPointGreaterThan(Integer reorderPoint);
    Item save(Item item);
    Item update(UUID id, Item item) throws NotFoundException;
    void delete(UUID id) throws NotFoundException;

    // Admin operations for database altering
    void recreateTable();
    void truncateTable();
    void backupData();
}
