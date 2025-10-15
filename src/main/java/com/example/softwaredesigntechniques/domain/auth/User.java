package com.example.softwaredesigntechniques.domain.auth;

import com.example.softwaredesigntechniques.domain.common.AuditedEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User extends AuditedEntity {

    @NotBlank
    @Size(max = 50)
    @Column(name = "username", unique = true, nullable = false)
    private String username;

    @NotBlank
    @Size(max = 255)
    @Column(name = "password", nullable = false)
    private String password;

    @NotBlank
    @Email
    @Size(max = 100)
    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "enabled", nullable = false)
    private Boolean enabled = true;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    public User() {}

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }
}
