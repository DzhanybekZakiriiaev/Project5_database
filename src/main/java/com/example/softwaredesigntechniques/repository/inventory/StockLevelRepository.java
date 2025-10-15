package com.example.softwaredesigntechniques.repository.inventory;

import com.example.softwaredesigntechniques.domain.inventory.StockLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface StockLevelRepository extends JpaRepository<StockLevel, UUID> {
    List<StockLevel> findByLevelLessThan(Integer level);
}
