package com.example.softwaredesigntechniques.domain.common;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@MappedSuperclass
public abstract class BaseEntity implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    BaseEntity that = (BaseEntity) o;

    return Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    int tempId = -1;

    if (Objects.nonNull(this.id)) {
      tempId = this.id.intValue();
    }

    return tempId * 5;
  }
} 