package com.example.softwaredesigntechniques.repository.inventory;

import com.example.softwaredesigntechniques.domain.inventory.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ItemRepository extends JpaRepository<Item, UUID> {
    Optional<Item> findBySku(String sku);
    List<Item> findByNameContainingIgnoreCase(String name);
    List<Item> findByReorderPointGreaterThan(Integer reorderPoint);
}
