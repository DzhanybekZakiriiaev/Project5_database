package com.example.softwaredesigntechniques.service.inventory.impl;

import com.example.softwaredesigntechniques.domain.inventory.StockLedger;
import com.example.softwaredesigntechniques.exception.NotFoundException;
import com.example.softwaredesigntechniques.repository.inventory.StockLedgerRepository;
import com.example.softwaredesigntechniques.service.inventory.StockLedgerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class StockLedgerServiceImpl implements StockLedgerService {

    @Autowired
    private StockLedgerRepository stockLedgerRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    @Transactional(readOnly = true)
    public StockLedger get(UUID id) throws NotFoundException {
        return stockLedgerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("StockLedger not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<StockLedger> getAll() {
        return stockLedgerRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<StockLedger> findByItemId(UUID itemId) {
        return stockLedgerRepository.findByItemId(itemId);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<StockLedger> findByRefId(String refId) {
        return stockLedgerRepository.findByRefId(refId);
    }

    @Override
    public StockLedger save(StockLedger stockLedger) {
        stockLedger.setCreatedAt(LocalDateTime.now());
        return stockLedgerRepository.save(stockLedger);
    }

    @Override
    public StockLedger update(UUID id, StockLedger stockLedger) throws NotFoundException {
        StockLedger existingStockLedger = get(id);
        existingStockLedger.setItemId(stockLedger.getItemId());
        existingStockLedger.setDelta(stockLedger.getDelta());
        existingStockLedger.setReason(stockLedger.getReason());
        existingStockLedger.setRefId(stockLedger.getRefId());
        return stockLedgerRepository.save(existingStockLedger);
    }

    @Override
    public void delete(UUID id) throws NotFoundException {
        StockLedger stockLedger = get(id);
        stockLedgerRepository.delete(stockLedger);
    }

    // Admin operations
    @Override
    public void recreateTable() {
        jdbcTemplate.execute("DROP TABLE IF EXISTS inventory.\"Stock_Ledger\" CASCADE");
        jdbcTemplate.execute("CREATE TABLE inventory.\"Stock_Ledger\" (" +
                "\"id\" UUID PRIMARY KEY," +
                "\"item_id\" UUID NOT NULL," +
                "\"delta\" INT NOT NULL," +
                "\"reason\" TEXT NOT NULL," +
                "\"ref_id\" TEXT UNIQUE NOT NULL," +
                "\"created_at\" TIMESTAMPTZ NOT NULL DEFAULT now()" +
                ")");
    }

    @Override
    public void truncateTable() {
        jdbcTemplate.execute("TRUNCATE TABLE inventory.\"Stock_Ledger\" CASCADE");
    }

    @Override
    public void backupData() {
        System.out.println("StockLedger data backup completed at: " + System.currentTimeMillis());
    }
}
