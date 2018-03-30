package app.dao.interf;

import app.model.Algorithm;
import app.model.AreaOfUse;
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

  void setName(Algorithm algorithm, String name) throws Exception;

  void setComplexity(Algorithm algorithm, String complexity) throws Exception;

  void setDesignParadigm(Algorithm algorithm, DesignParadigm designParadigm) throws Exception;

  void setFieldOfStudy(Algorithm algorithm, FieldOfStudy fieldOfStudy) throws Exception;

  void deleteById(int id) throws Exception;
}
