package app.service;

import app.model.DesignParadigm;
import app.model.FieldOfStudy;

import java.sql.SQLException;

public class AlgorithmService {

  public void createAlgorithm(String name, String complexity, String designParadigm, String fieldOfStudy){
    DesignParadigm paradigm = getParadigmByName(designParadigm);
    FieldOfStudy field = getFieldByName(fieldOfStudy);
    if (paradigm == null) {
      throw new SQLException("Can't find paradigm by " + designParadigm);
    } else if (field == null) {
      throw new SQLException("Can't find field of study by " + fieldOfStudy);
    }
  }
}
