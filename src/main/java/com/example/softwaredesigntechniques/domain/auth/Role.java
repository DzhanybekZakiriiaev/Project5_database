package com.example.softwaredesigntechniques.domain.auth;

import com.example.softwaredesigntechniques.domain.common.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@Entity
@Table(name = "roles")
public class Role extends BaseEntity {

    @NotBlank
    @Size(max = 50)
    @Column(name = "name", unique = true, nullable = false)
    private String name;

    @Size(max = 255)
    @Column(name = "description")
    private String description;

    public Role() {}

    public Role(String name) {
        this.name = name;
    }

    public Role(String name, String description) {
        this.name = name;
        this.description = description;
    }
}
