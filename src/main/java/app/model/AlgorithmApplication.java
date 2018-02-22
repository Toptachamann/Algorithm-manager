package app.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "algorithm_application")
public class AlgorithmApplication {

  @Id
  @Column(name = "app_algorithm_id")
  private int algorithmId;

  @Id
  @Column(name = "app_area_Id")
  private int areaId;

  public AlgorithmApplication() {

  }

  public AlgorithmApplication(int algorithmId, int areaId) {

    this.algorithmId = algorithmId;
    this.areaId = areaId;
  }

  public int getAlgorithmId() {
    return algorithmId;
  }

  public void setAlgorithmId(int algorithmId) {
    this.algorithmId = algorithmId;
  }

  public int getAreaId() {
    return areaId;
  }

  public void setAreaId(int areaId) {
    this.areaId = areaId;
  }

}
