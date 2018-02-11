package app.model;

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

  @Column(name = "paradigm")
  private String paradigm;

  @Column(name = "description")
  private String description;

  public DesignParadigm(int id, String paradigm, String description) {
    this.id = id;
    this.paradigm = paradigm;
    this.description = description;
  }

  public DesignParadigm() {}

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
}
