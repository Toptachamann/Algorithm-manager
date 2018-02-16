package app.service;

import app.auxiliary.LogicException;
import app.model.Algorithm;
import app.model.DesignParadigm;
import app.model.FieldOfStudy;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface AlgorithmService {
  List<Algorithm> getAllAlgorithms() throws SQLException;

  List<DesignParadigm> getDesignParadigms() throws SQLException;

  List<FieldOfStudy> getAllFieldsOfStudy() throws SQLException;

  DesignParadigm insertDesignParadigm(String paradigmName) throws SQLException, LogicException;

  DesignParadigm insertDesignParadigm(String paradigmName, String description) throws SQLException, LogicException;

  Optional<FieldOfStudy> getFieldByName(String name) throws SQLException;

  Optional<DesignParadigm> getParadigmByName(String name) throws SQLException;

  FieldOfStudy insertFieldOfStudy(String fieldName) throws SQLException, LogicException;

  FieldOfStudy insertFieldOfStudy(String fieldName, String fieldDescription)
      throws SQLException, LogicException;

  Algorithm createAlgorithm(
      String name, String complexity, DesignParadigm designParadigm, FieldOfStudy fieldOfStudy)
      throws SQLException, LogicException;

  List<Algorithm> searchAlgorithm(
      String text, String text1, DesignParadigm selectedItem, FieldOfStudy selectedItem1)
      throws SQLException;

  void updateAlgorithmName(Algorithm algorithm, String newName) throws SQLException, LogicException;

  void updateAlgorithmComplexity(Algorithm rowValue, String newComplexity) throws SQLException;

  DesignParadigm updateDesignParadigm(DesignParadigm oldValue, String paradigm) throws SQLException;

  FieldOfStudy updateFieldName(FieldOfStudy newValue, String field) throws SQLException;

  void setDesignParadigm(Algorithm algorithm, DesignParadigm paradigm) throws SQLException;

  void setFieldOfStudy(Algorithm algorithm, FieldOfStudy field) throws SQLException;

  void deleteItem(Algorithm algorithm) throws SQLException;
}
