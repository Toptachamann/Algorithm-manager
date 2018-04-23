package com.algorithm.manager.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "area_of_use")
public class AreaOfUse {

  @Id
  @Column(name = "area_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @Column(name = "area", length = 50, nullable = false, unique = true)
  private String name;

  @Column(name = "description")
  private String description;

  public AreaOfUse(String name, String description) {
    this.name = name;
    this.description = description;
  }

  public AreaOfUse(int id, String name) {
    this.id = id;
    this.name = name;
  }

  public AreaOfUse(int id, String name, String description) {

    this.id = id;
    this.name = name;
    this.description = description;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;

    if (o == null || getClass() != o.getClass()) return false;

    AreaOfUse areaOfUse1 = (AreaOfUse) o;

    return new EqualsBuilder()
        .append(id, areaOfUse1.id)
        .append(name, areaOfUse1.name)
        .append(description, areaOfUse1.description)
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
        .append(id)
        .append(name)
        .append(description)
        .toHashCode();
  }

  public AreaOfUse(String name) {
    this.name = name;
  }

  public AreaOfUse() {}

  @Override
  public String toString() {
    return new ToStringBuilder(this)
        .append("id", id)
        .append("areaOfUse", name)
        .append("description", description)
        .toString();
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String areaOfUse) {
    this.name = areaOfUse;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }
}
