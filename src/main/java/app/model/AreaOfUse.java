package app.model;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.List;

public class AreaOfUse {
  private int id;
  private String areaOfUse;
  private String description;

  public AreaOfUse(int id, String areaOfUse) {
    this.id = id;
    this.areaOfUse = areaOfUse;
  }

  public AreaOfUse(int id, String areaOfUse, String description) {

    this.id = id;
    this.areaOfUse = areaOfUse;
    this.description = description;
  }

  public AreaOfUse(int id, String areaOfUse, String description, List<Algorithm> algorithms) {

    this.id = id;
    this.areaOfUse = areaOfUse;
    this.description = description;
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
