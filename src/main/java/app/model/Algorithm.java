package app.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "algorithm")
public class Algorithm {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "algorithm_id")
  private int id;

  @Column(name = "algorithm", unique = true)
  private String name;

  @Column(name = "complexity")
  private String complexity;

  @Transient
  private DesignParadigm designParadigm;

  @Transient
  private FieldOfStudy fieldOfStudy;

  public Algorithm(
      int id, String name, String complexity, DesignParadigm designParadigm, FieldOfStudy fieldOfStudy) {
    this.id = id;
    this.name = name;
    this.complexity = complexity;
    this.designParadigm = designParadigm;
    this.fieldOfStudy = fieldOfStudy;
  }

  public Algorithm() {}

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
}
