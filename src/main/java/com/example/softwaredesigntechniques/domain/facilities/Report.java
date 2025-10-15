package com.example.softwaredesigntechniques.domain.facilities;

import com.example.softwaredesigntechniques.domain.common.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@Entity
@Table(name = "reports")
public class Report extends BaseEntity {

    @NotBlank
    @Size(max = 255)
    @Column(name = "report_text", nullable = false)
    private String reportText;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "log_id", nullable = false)
    private Log log;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "part_id")
    private Part part;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "machine_id")
    private Machine machine;

    @NotNull
    @Column(name = "needs_repair", nullable = false)
    private Boolean needsRepair;

    public Report() {}

    public Report(String reportText, Log log, Boolean needsRepair) {
        this.reportText = reportText;
        this.log = log;
        this.needsRepair = needsRepair;
    }

    public Report(String reportText, Log log, Part part, Machine machine, Boolean needsRepair) {
        this.reportText = reportText;
        this.log = log;
        this.part = part;
        this.machine = machine;
        this.needsRepair = needsRepair;
    }
}
