package app.model;

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
