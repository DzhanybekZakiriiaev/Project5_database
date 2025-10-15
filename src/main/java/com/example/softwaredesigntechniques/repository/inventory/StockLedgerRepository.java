package com.example.softwaredesigntechniques.repository.inventory;

import com.example.softwaredesigntechniques.domain.inventory.StockLedger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface StockLedgerRepository extends JpaRepository<StockLedger, UUID> {
    List<StockLedger> findByItemId(UUID itemId);
    Optional<StockLedger> findByRefId(String refId);
}
