package com.example.softwaredesigntechniques.domain.inventory;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "stock_ledger", schema = "inventory")
public class StockLedger implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", columnDefinition = "UUID")
    private UUID id;

    @NotNull
    @Column(name = "item_id", nullable = false)
    private UUID itemId;

    @NotNull
    @Column(name = "delta", nullable = false)
    private Integer delta;

    @NotBlank
    @Column(name = "reason", nullable = false)
    private String reason;

    @NotBlank
    @Column(name = "ref_id", unique = true, nullable = false)
    private String refId;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", insertable = false, updatable = false)
    private Item item;

    public StockLedger() {}

    public StockLedger(UUID itemId, Integer delta, String reason, String refId) {
        this.itemId = itemId;
        this.delta = delta;
        this.reason = reason;
        this.refId = refId;
        this.createdAt = LocalDateTime.now();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StockLedger that = (StockLedger) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
