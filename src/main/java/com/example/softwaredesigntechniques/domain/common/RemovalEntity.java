package com.example.softwaredesigntechniques.domain.common;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@MappedSuperclass
public abstract class RemovalEntity extends AuditedEntity {

  @Column(name = "deleted_at")
  private LocalDateTime deletedAt;
} 