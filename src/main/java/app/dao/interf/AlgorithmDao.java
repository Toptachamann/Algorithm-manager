package app.dao.interf;

import app.model.Algorithm;
import app.model.DesignParadigm;
import app.model.FieldOfStudy;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface AlgorithmDao {
  void persist(Algorithm algorithm) throws SQLException;

  List<Algorithm> getAllAlgorithms() throws Exception;

  Optional<Algorithm> getAlgorithmByName(String name) throws Exception;

  default boolean containsAlgorithm(String name) throws Exception {
    return getAlgorithmByName(name).isPresent();
  }

  List<Algorithm> searchAlgorithm(
      String algorithm, String complexity, DesignParadigm designParadigm, FieldOfStudy fieldOfStudy)
      throws Exception;

  void merge(Algorithm algorithm) throws Exception;

  void delete(Algorithm algorithm) throws Exception;
}
