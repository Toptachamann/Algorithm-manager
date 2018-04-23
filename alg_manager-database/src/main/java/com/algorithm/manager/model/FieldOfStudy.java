package com.algorithm.manager.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.jetbrains.annotations.Nullable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "field_of_study")
public class FieldOfStudy {
  @Id
  @Column(name = "field_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @Column(name = "field", nullable = false, unique = true, length = 50)
  private String name;

  @Column(name = "description")
  private String description;

  public FieldOfStudy(String name) {
    this.name = name;
  }

  public FieldOfStudy(String name, @Nullable String description) {

    this.name = name;
    this.description = description;
  }

  public FieldOfStudy(int id, String name, @Nullable String description) {

    this.id = id;
    this.name = name;
    this.description = description;
  }

  public FieldOfStudy() {}

  public FieldOfStudy(int id, String name) {

    this.id = id;
    this.name = name;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;

    if (o == null || getClass() != o.getClass()) return false;

    FieldOfStudy that = (FieldOfStudy) o;

    return new EqualsBuilder()
        .append(id, that.id)
        .append(name, that.name)
        .append(description, that.description)
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37).append(id).append(name).append(description).toHashCode();
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

  public void setName(String field) {
    this.name = field;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this)
        .append("id", id)
        .append("field", name)
        .append("description", description)
        .toString();
  }
}
