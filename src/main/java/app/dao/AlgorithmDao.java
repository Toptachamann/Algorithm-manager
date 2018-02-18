package app.dao;

import app.model.Algorithm;
import app.model.DesignParadigm;
import app.model.FieldOfStudy;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface AlgorithmDao {
  Algorithm insertAlgorithm(
      String name, String complexity, DesignParadigm designParadigm, FieldOfStudy fieldOfStudy)
      throws SQLException;

  List<Algorithm> getAllAlgorithms() throws SQLException;

  Optional<Algorithm> getAlgorithmByName(String name) throws SQLException;

  List<Algorithm> searchAlgorithm(
      String algorithm, String complexity, DesignParadigm designParadigm, FieldOfStudy fieldOfStudy)
      throws SQLException;

  void setDesignParadigm(int paradigmId, int algorithmId) throws SQLException;

  void setFieldOfStudy(int fieldId, int algorithmId) throws SQLException;

  void updateEntry(String column, String value, int id) throws SQLException;

  void deleteById(int id) throws SQLException;

  List<Algorithm> getAlgorithmsByArea(int areaId) throws SQLException;
}
