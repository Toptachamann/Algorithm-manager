package app.model;

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
  private String areaOfUse;

  @Column(name = "description")
  private String description;

  public AreaOfUse(String areaOfUse, String description) {
    this.areaOfUse = areaOfUse;
    this.description = description;
  }

  public AreaOfUse(int id, String areaOfUse) {
    this.id = id;
    this.areaOfUse = areaOfUse;
  }

  public AreaOfUse(int id, String areaOfUse, String description) {

    this.id = id;
    this.areaOfUse = areaOfUse;
    this.description = description;
  }

  public AreaOfUse(String areaOfUse) {
    this.areaOfUse = areaOfUse;
  }

  public AreaOfUse() {}

  @Override
  public String toString() {
    return new ToStringBuilder(this)
        .append("id", id)
        .append("areaOfUse", areaOfUse)
        .append("description", description)
        .toString();
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getAreaOfUse() {
    return areaOfUse;
  }

  public void setAreaOfUse(String areaOfUse) {
    this.areaOfUse = areaOfUse;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }
}
