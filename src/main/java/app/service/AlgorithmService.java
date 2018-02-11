package app.service;

import app.model.Algorithm;
import app.model.DesignParadigm;
import app.model.FieldOfStudy;

import java.sql.SQLException;
import java.util.List;

public interface AlgorithmService {
  List<Algorithm> getAlgorithms() throws SQLException;

  Algorithm getById(int id);

  Algorithm getAlgorithmByName(String name) throws SQLException;

  <T> void updateEntry(String table, String column, String value, String primaryKeyColumn, T id)
      throws SQLException;

  List<Algorithm> searchAlgorithm(
      String algorithm, String complexity, String designParadigm, String fieldOfStudy)
      throws SQLException;

  List<DesignParadigm> getDesignParadigms() throws SQLException;

  List<FieldOfStudy> getFieldsOfStudy() throws SQLException;

  Algorithm insertAlgorithm(
      String algoName, String algoComplexity, String designParadigm, String fieldOfStudy)
      throws SQLException;

  <T> void deleteItem(String table, String keyColumn, T value) throws SQLException;

  void insertParadigm(String paradigm) throws SQLException;

  void insertParadigm(String paradigm, String description) throws SQLException;

  void insertFieldOfStudy(String fieldOfStudy) throws SQLException;

  void insertFieldOfStudy(String fieldOfStudy, String description) throws SQLException;

  DesignParadigm getParadigmByName(String name) throws SQLException;

  FieldOfStudy getFieldByName(String name) throws SQLException;
}
