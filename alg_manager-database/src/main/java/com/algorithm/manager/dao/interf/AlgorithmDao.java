package com.algorithm.manager.dao.interf;

import com.algorithm.manager.model.Algorithm;
import com.algorithm.manager.model.DesignParadigm;
import com.algorithm.manager.model.FieldOfStudy;
import org.jetbrains.annotations.Nullable;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface AlgorithmDao {
  int persist(Algorithm algorithm) throws SQLException;

  List<Algorithm> getAlgorithms() throws Exception;

  Optional<Algorithm> getAlgorithmById(int id) throws Exception;

  Optional<Algorithm> getAlgorithmByName(String name) throws Exception;

  default boolean containsAlgorithmWithId(int id) throws Exception {
    return getAlgorithmById(id).isPresent();
  }

  default boolean containsAlgorithmWithName(String name) throws Exception {
    return getAlgorithmByName(name).isPresent();
  }

  List<Algorithm> searchAlgorithm(
      @Nullable String algorithm,
      @Nullable String complexity,
      @Nullable DesignParadigm designParadigm,
      @Nullable FieldOfStudy fieldOfStudy)
      throws Exception;

  void merge(Algorithm algorithm) throws Exception;

  void setName(int id, String name) throws Exception;

  void setComplexity(int id, String complexity) throws Exception;

  void setDesignParadigm(int id, DesignParadigm designParadigm) throws Exception;

  void setFieldOfStudy(int id, FieldOfStudy fieldOfStudy) throws Exception;

  void delete(Algorithm algorithm) throws Exception;

  void deleteById(int id) throws Exception;

  void deleteByName(String name) throws Exception;
}
