package com.example.softwaredesigntechniques.domain.facilities;

import com.example.softwaredesigntechniques.domain.common.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "\"Logs\"")
public class Log extends BaseEntity {

    @NotNull
    @Column(name = "\"date\"", nullable = false)
    private LocalDate date;

    @OneToMany(mappedBy = "log", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Report> reports;

    public Log() {}

    public Log(LocalDate date) {
        this.date = date;
    }
}
