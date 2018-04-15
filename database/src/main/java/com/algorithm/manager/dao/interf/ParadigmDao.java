package com.algorithm.manager.dao.interf;

import com.algorithm.manager.model.DesignParadigm;

import java.util.List;
import java.util.Optional;

public interface ParadigmDao {
  void persist(DesignParadigm paradigm) throws Exception;

  List<DesignParadigm> getAllParadigms() throws Exception;

  Optional<DesignParadigm> getParadigmById(int id) throws Exception;

  Optional<DesignParadigm> getParadigmByParadigm(String paradigm) throws Exception;

  default boolean containsParadigm(DesignParadigm paradigm) throws Exception {
    return getParadigmById(paradigm.getId()).isPresent();
  }

  default boolean containsParadigmWithName(String name) throws Exception {
    return getParadigmByParadigm(name).isPresent();
  }

  void merge(DesignParadigm paradigm) throws Exception;

  void delete(DesignParadigm paradigm) throws Exception;
}
