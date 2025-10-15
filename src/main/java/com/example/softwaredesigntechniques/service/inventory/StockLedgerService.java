package com.example.softwaredesigntechniques.service.inventory;

import com.example.softwaredesigntechniques.domain.inventory.StockLedger;
import com.example.softwaredesigntechniques.exception.NotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface StockLedgerService {
    // CRUD operations available to all users
    StockLedger get(UUID id) throws NotFoundException;
    List<StockLedger> getAll();
    List<StockLedger> findByItemId(UUID itemId);
    Optional<StockLedger> findByRefId(String refId);
    StockLedger save(StockLedger stockLedger);
    StockLedger update(UUID id, StockLedger stockLedger) throws NotFoundException;
    void delete(UUID id) throws NotFoundException;

    // Admin operations for database altering
    void recreateTable();
    void truncateTable();
    void backupData();
}
