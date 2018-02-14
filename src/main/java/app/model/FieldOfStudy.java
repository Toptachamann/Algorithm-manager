package app.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class FieldOfStudy {
  private int id;
  private String field;
  private String description;

  public FieldOfStudy(int id, String field, String description) {

    this.id = id;
    this.field = field;
    this.description = description;
  }

  public FieldOfStudy() {}

  public FieldOfStudy(int id, String field) {

    this.id = id;
    this.field = field;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;

    if (o == null || getClass() != o.getClass()) return false;

    FieldOfStudy that = (FieldOfStudy) o;

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

  public String getField() {
    return field;
  }

  public void setField(String field) {
    this.field = field;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }
}
