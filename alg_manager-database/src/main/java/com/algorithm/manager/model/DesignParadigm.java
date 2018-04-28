package com.algorithm.manager.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.jetbrains.annotations.Nullable;
import org.springframework.hateoas.ResourceSupport;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "design_paradigm")
public class DesignParadigm extends ResourceSupport {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "paradigm_id")
  private int designParadigmId;

  @Column(name = "paradigm", unique = true, nullable = false, length = 50)
  private String name;

  @Column(name = "description")
  private String description;

  public DesignParadigm(int designParadigmId, String name) {
    this.designParadigmId = designParadigmId;
    this.name = name;
  }

  public DesignParadigm(int designParadigmId, String name, @Nullable String description) {
    this.designParadigmId = designParadigmId;
    this.name = name;
    this.description = description;
  }

  public DesignParadigm() {}

  public DesignParadigm(String name, @Nullable String description) {
    this.name = name;
    this.description = description;
  }

  public DesignParadigm(String name) {

    this.name = name;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;

    if (o == null || getClass() != o.getClass()) return false;

    DesignParadigm that = (DesignParadigm) o;

    return new EqualsBuilder()
        .append(designParadigmId, that.designParadigmId)
        .append(name, that.name)
        .append(description, that.description)
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37).append(designParadigmId).append(name).append(description).toHashCode();
  }

  public int getDesignParadigmId() {
    return designParadigmId;
  }

  public void setDesignParadigmId(int id) {
    this.designParadigmId = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String paradigm) {
    this.name = paradigm;
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
        .append("id", designParadigmId)
        .append("paradigm", name)
        .append("description", description)
        .toString();
  }
}
