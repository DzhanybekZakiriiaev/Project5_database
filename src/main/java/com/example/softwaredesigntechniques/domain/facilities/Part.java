package com.example.softwaredesigntechniques.domain.facilities;

import com.example.softwaredesigntechniques.domain.common.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "\"Parts\"")
public class Part extends BaseEntity {

    @NotBlank
    @Size(max = 255)
    @Column(name = "\"name\"", nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "\"machine_id\"")
    private Machine machine;

    @OneToMany(mappedBy = "part", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Report> reports;

    public Part() {}

    public Part(String name) {
        this.name = name;
    }

    public Part(String name, Machine machine) {
        this.name = name;
        this.machine = machine;
    }
}
