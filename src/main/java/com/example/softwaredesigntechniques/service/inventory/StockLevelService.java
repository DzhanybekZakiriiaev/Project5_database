package com.example.softwaredesigntechniques.service.inventory;

import com.example.softwaredesigntechniques.domain.inventory.StockLevel;
import com.example.softwaredesigntechniques.exception.NotFoundException;

import java.util.List;
import java.util.UUID;

public interface StockLevelService {
    // CRUD operations available to all users
    StockLevel get(UUID itemId) throws NotFoundException;
    List<StockLevel> getAll();
    List<StockLevel> findByLevelLessThan(Integer level);
    StockLevel save(StockLevel stockLevel);
    StockLevel update(UUID itemId, StockLevel stockLevel) throws NotFoundException;
    void delete(UUID itemId) throws NotFoundException;

    // Admin operations for database altering
    void recreateTable();
    void truncateTable();
    void backupData();
}
