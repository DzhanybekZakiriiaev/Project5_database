package com.example.softwaredesigntechniques.service.inventory.impl;

import com.example.softwaredesigntechniques.domain.inventory.StockLevel;
import com.example.softwaredesigntechniques.exception.NotFoundException;
import com.example.softwaredesigntechniques.repository.inventory.StockLevelRepository;
import com.example.softwaredesigntechniques.service.inventory.StockLevelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class StockLevelServiceImpl implements StockLevelService {

    @Autowired
    private StockLevelRepository stockLevelRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    @Transactional(readOnly = true)
    public StockLevel get(UUID itemId) throws NotFoundException {
        return stockLevelRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("StockLevel not found with itemId: " + itemId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<StockLevel> getAll() {
        return stockLevelRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<StockLevel> findByLevelLessThan(Integer level) {
        return stockLevelRepository.findByLevelLessThan(level);
    }

    @Override
    public StockLevel save(StockLevel stockLevel) {
        stockLevel.setUpdatedAt(LocalDateTime.now());
        return stockLevelRepository.save(stockLevel);
    }

    @Override
    public StockLevel update(UUID itemId, StockLevel stockLevel) throws NotFoundException {
        StockLevel existingStockLevel = get(itemId);
        existingStockLevel.setLevel(stockLevel.getLevel());
        existingStockLevel.setUpdatedAt(LocalDateTime.now());
        return stockLevelRepository.save(existingStockLevel);
    }

    @Override
    public void delete(UUID itemId) throws NotFoundException {
        StockLevel stockLevel = get(itemId);
        stockLevelRepository.delete(stockLevel);
    }

    // Admin operations
    @Override
    public void recreateTable() {
        jdbcTemplate.execute("DROP TABLE IF EXISTS inventory.\"Stock_Levels\" CASCADE");
        jdbcTemplate.execute("CREATE TABLE inventory.\"Stock_Levels\" (" +
                "\"item_id\" UUID PRIMARY KEY," +
                "\"level\" INT NOT NULL DEFAULT 0," +
                "\"updated_at\" TIMESTAMPTZ NOT NULL DEFAULT now()" +
                ")");
    }

    @Override
    public void truncateTable() {
        jdbcTemplate.execute("TRUNCATE TABLE inventory.\"Stock_Levels\" CASCADE");
    }

    @Override
    public void backupData() {
        System.out.println("StockLevel data backup completed at: " + System.currentTimeMillis());
    }
}
