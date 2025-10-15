package com.example.softwaredesigntechniques.domain.inventory;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "\"Stock_Levels\"", schema = "inventory")
public class StockLevel implements Serializable {

    @Id
    @Column(name = "\"item_id\"")
    private UUID itemId;

    @NotNull
    @Column(name = "\"level\"", nullable = false)
    private Integer level = 0;

    @Column(name = "\"updated_at\"")
    private LocalDateTime updatedAt;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "\"item_id\"")
    @MapsId
    private Item item;

    public StockLevel() {}

    public StockLevel(Item item, Integer level) {
        this.item = item;
        this.itemId = item.getId();
        this.level = level;
        this.updatedAt = LocalDateTime.now();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StockLevel that = (StockLevel) o;
        return Objects.equals(itemId, that.itemId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(itemId);
    }
}
