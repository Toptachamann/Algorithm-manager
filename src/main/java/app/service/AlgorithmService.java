package app.service;

import app.model.Algorithm;
import app.model.DesignParadigm;
import app.model.FieldOfStudy;

import java.sql.SQLException;
import java.util.List;

public interface AlgorithmService {
  List<Algorithm> getAllAlgorithms() throws SQLException;

  List<DesignParadigm> getDesignParadigms() throws SQLException;

  List<FieldOfStudy> getAllFieldsOfStudy() throws SQLException;

  DesignParadigm insertDesignParadigm(String paradigmName, String description) throws SQLException;

  FieldOfStudy insertFieldOfStudy(String retrievedName, String retrievedDescription)
      throws SQLException;

  Algorithm createAlgorithm(
      String name, String complexity, DesignParadigm designParadigm, FieldOfStudy fieldOfStudy)
      throws SQLException;

  List<Algorithm> searchAlgorithm(
      String text, String text1, DesignParadigm selectedItem, FieldOfStudy selectedItem1)
      throws SQLException;

  FieldOfStudy insertFieldOfStudy(String fieldName) throws SQLException;

  void deleteItem(Algorithm algorithm) throws SQLException;

  void updateAlgorithmName(Algorithm algorithm, String newName) throws SQLException;

  void updateAlgorithmComplexity(Algorithm rowValue, String newComplexity) throws SQLException;

  DesignParadigm updateDesignParadigm(DesignParadigm oldValue, String paradigm) throws SQLException;

  FieldOfStudy updateFieldOfStudy(FieldOfStudy newValue, String field) throws SQLException;
}
