package app.model;

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
@Table(name = "design_paradigm")
public class DesignParadigm {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "paradigm_id")
  private int id;

  @Column(name = "paradigm", unique = true, nullable = false, length = 50)
  private String paradigm;

  @Column(name = "description")
  private String description;

  public DesignParadigm(int id, String paradigm) {
    this.id = id;
    this.paradigm = paradigm;
  }

  public DesignParadigm(int id, String paradigm, String description) {
    this.id = id;
    this.paradigm = paradigm;
    this.description = description;
  }

  public DesignParadigm() {}

  public DesignParadigm(String paradigm, String description) {
    this.paradigm = paradigm;
    this.description = description;
  }

  public DesignParadigm(String paradigm) {

    this.paradigm = paradigm;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;

    if (o == null || getClass() != o.getClass()) return false;

    DesignParadigm that = (DesignParadigm) o;

    return new EqualsBuilder().append(id, that.id).isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37).append(id).toHashCode();
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getParadigm() {
    return paradigm;
  }

  public void setParadigm(String paradigm) {
    this.paradigm = paradigm;
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
        .append("paradigm", paradigm)
        .append("description", description)
        .toString();
  }
}
