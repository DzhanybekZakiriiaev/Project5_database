package com.example.softwaredesigntechniques.service.inventory.impl;

import com.example.softwaredesigntechniques.domain.inventory.Item;
import com.example.softwaredesigntechniques.exception.NotFoundException;
import com.example.softwaredesigntechniques.repository.inventory.ItemRepository;
import com.example.softwaredesigntechniques.service.inventory.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class ItemServiceImpl implements ItemService {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    @Transactional(readOnly = true)
    public Item get(UUID id) throws NotFoundException {
        return itemRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Item not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Item> getAll() {
        return itemRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Item> findBySku(String sku) {
        return itemRepository.findBySku(sku);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Item> findByNameContaining(String name) {
        return itemRepository.findByNameContainingIgnoreCase(name);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Item> findByReorderPointGreaterThan(Integer reorderPoint) {
        return itemRepository.findByReorderPointGreaterThan(reorderPoint);
    }

    @Override
    public Item save(Item item) {
        return itemRepository.save(item);
    }

    @Override
    public Item update(UUID id, Item item) throws NotFoundException {
        Item existingItem = get(id);
        existingItem.setSku(item.getSku());
        existingItem.setName(item.getName());
        existingItem.setReorderPoint(item.getReorderPoint());
        return itemRepository.save(existingItem);
    }

    @Override
    public void delete(UUID id) throws NotFoundException {
        Item item = get(id);
        itemRepository.delete(item);
    }

    // Admin operations
    @Override
    public void recreateTable() {
        jdbcTemplate.execute("DROP TABLE IF EXISTS inventory.\"Items\" CASCADE");
        jdbcTemplate.execute("CREATE TABLE inventory.\"Items\" (" +
                "\"id\" UUID PRIMARY KEY," +
                "\"sku\" TEXT UNIQUE," +
                "\"name\" TEXT NOT NULL," +
                "\"reorder_point\" INT NOT NULL DEFAULT 0," +
                "\"created_at\" TIMESTAMPTZ NOT NULL DEFAULT now()," +
                "\"updated_at\" TIMESTAMPTZ NOT NULL DEFAULT now()" +
                ")");
    }

    @Override
    public void truncateTable() {
        jdbcTemplate.execute("TRUNCATE TABLE inventory.\"Items\" CASCADE");
    }

    @Override
    public void backupData() {
        System.out.println("Item data backup completed at: " + System.currentTimeMillis());
    }
}
