package com.algorithm.manager.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;

@Entity
@Table(name = "algorithm_application")
public class Application {

  @Id
  @Column(name = "application_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @ManyToOne(cascade = {CascadeType.REFRESH}, optional = false)
  @JoinColumn(name = "app_algorithm_id", referencedColumnName = "algorithm_id",
      nullable = false, foreignKey = @ForeignKey(name = "fk_algorithm_application"))
  private Algorithm algorithm;
  @ManyToOne(cascade = {CascadeType.REFRESH}, optional = false)
  @JoinColumn(name = "app_area_id", referencedColumnName = "area_id",
      nullable = false, foreignKey = @ForeignKey(name = "fk_area_application"))
  private AreaOfUse areaOfUse;

  public Application(int id, Algorithm algorithm, AreaOfUse areaOfUse) {
    this.id = id;
    this.algorithm = algorithm;
    this.areaOfUse = areaOfUse;
  }

  public Application(Algorithm algorithm, AreaOfUse areaOfUse) {
    this.algorithm = algorithm;
    this.areaOfUse = areaOfUse;
  }

  public Application() {

  }

  @Override
  public String toString() {
    return new ToStringBuilder(this)
        .append("applicationId", id)
        .append("algorithm", algorithm)
        .append("areaOfUse", areaOfUse)
        .toString();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;

    if (o == null || getClass() != o.getClass()) return false;

    Application that = (Application) o;

    return new EqualsBuilder()
        .append(id, that.id)
        .append(algorithm, that.algorithm)
        .append(areaOfUse, that.areaOfUse)
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
        .append(id)
        .append(algorithm)
        .append(areaOfUse)
        .toHashCode();
  }

  public int getId() {
    return id;
  }

  public void setId(int applicationId) {
    this.id = applicationId;
  }

  public Algorithm getAlgorithm() {
    return algorithm;
  }

  public void setAlgorithm(Algorithm algorithm) {
    this.algorithm = algorithm;
  }

  public AreaOfUse getAreaOfUse() {
    return areaOfUse;
  }

  public void setAreaOfUse(AreaOfUse areaOfUse) {
    this.areaOfUse = areaOfUse;
  }
}
