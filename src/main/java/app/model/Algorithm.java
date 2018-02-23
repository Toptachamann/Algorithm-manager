package app.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;

@Entity
@Table(name = "algorithm")
public class Algorithm {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "algorithm_id")
  private int id;

  @Column(name = "algorithm", unique = true, nullable = false, length = 50)
  private String name;

  @Column(name = "complexity", nullable = false, length = 50)
  private String complexity;

  @ManyToOne(
    cascade = {CascadeType.PERSIST, CascadeType.REFRESH},
    optional = false,
    targetEntity = DesignParadigm.class
  )
  @JoinColumn(
    name = "algo_paradigm_id",
    referencedColumnName = "paradigm_id",
    nullable = false,
    foreignKey = @ForeignKey(name = "fk_paradigm_algorithm")
  )
  private DesignParadigm designParadigm;
  @ManyToOne(
    cascade = {CascadeType.PERSIST, CascadeType.REFRESH},
    optional = false,
    targetEntity = FieldOfStudy.class
  )
  @JoinColumn(
    name = "algo_field_id",
    referencedColumnName = "field_id",
    nullable = false,
    foreignKey = @ForeignKey(name = "fk_field_id")
  )
  private FieldOfStudy fieldOfStudy;

  public Algorithm(
      String name, String complexity, DesignParadigm designParadigm, FieldOfStudy fieldOfStudy) {
    this.name = name;
    this.complexity = complexity;
    this.designParadigm = designParadigm;
    this.fieldOfStudy = fieldOfStudy;
  }

  public Algorithm(
      int id,
      String name,
      String complexity,
      DesignParadigm designParadigm,
      FieldOfStudy fieldOfStudy) {
    this.id = id;
    this.name = name;
    this.complexity = complexity;
    this.designParadigm = designParadigm;
    this.fieldOfStudy = fieldOfStudy;
  }

  public Algorithm() {}

  @Override
  public String toString() {
    return new ToStringBuilder(this)
        .append("id", id)
        .append("name", name)
        .append("complexity", complexity)
        .append("designParadigm", designParadigm)
        .append("fieldOfStudy", fieldOfStudy)
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

  public void setName(String name) {
    this.name = name;
  }

  public String getComplexity() {
    return complexity;
  }

  public void setComplexity(String complexity) {
    this.complexity = complexity;
  }

  public DesignParadigm getDesignParadigm() {
    return designParadigm;
  }

  public void setDesignParadigm(DesignParadigm designParadigm) {
    this.designParadigm = designParadigm;
  }

  public FieldOfStudy getFieldOfStudy() {
    return fieldOfStudy;
  }

  public void setFieldOfStudy(FieldOfStudy fieldOfStudy) {
    this.fieldOfStudy = fieldOfStudy;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;

    if (o == null || getClass() != o.getClass()) return false;

    Algorithm algorithm = (Algorithm) o;

    return new EqualsBuilder().append(id, algorithm.id).isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37).append(id).toHashCode();
  }
}
