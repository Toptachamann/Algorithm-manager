package app.dao.interf;

import app.model.Algorithm;
import app.model.DesignParadigm;
import app.model.FieldOfStudy;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface AlgorithmDao {
  Algorithm createAlgorithm(
      String name, String complexity, DesignParadigm designParadigm, FieldOfStudy fieldOfStudy)
      throws SQLException;

  List<Algorithm> getAllAlgorithms() throws Exception;

  Optional<Algorithm> getAlgorithmByName(String name) throws Exception;

  List<Algorithm> searchAlgorithm(
      String algorithm, String complexity, DesignParadigm designParadigm, FieldOfStudy fieldOfStudy)
      throws Exception;

  void setDesignParadigm(int paradigmId, int algorithmId) throws Exception;

  void setFieldOfStudy(int fieldId, int algorithmId) throws Exception;

  void updateEntry(String column, String value, int id) throws Exception;

  void deleteById(int id) throws Exception;

  List<Algorithm> getAlgorithmsByArea(int areaId) throws Exception;
}
