package app.dao;

import app.model.Algorithm;
import app.model.DesignParadigm;
import app.model.FieldOfStudy;

import java.sql.SQLException;
import java.util.List;

public interface AlgorithmDao {
  Algorithm insertAlgorithm(
      String name, String complexity, DesignParadigm designParadigm, FieldOfStudy fieldOfStudy)
      throws SQLException;

  List<Algorithm> getAllAlgorithms() throws SQLException;

  Algorithm getAlgorithmByName(String name) throws SQLException;

  List<Algorithm> searchAlgorithm(
      String algorithm, String complexity, String designParadigm, String fieldOfStudy)
  throws SQLException;

  void updateEntry(String column, String value, int id) throws SQLException;

  void deleteById(int id) throws SQLException;
}
