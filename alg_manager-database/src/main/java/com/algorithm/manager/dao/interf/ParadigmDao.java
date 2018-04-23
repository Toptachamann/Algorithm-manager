package com.algorithm.manager.dao.interf;

import com.algorithm.manager.model.DesignParadigm;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public interface ParadigmDao {
  int persist(DesignParadigm paradigm) throws Exception;

  DesignParadigm persist(String name, @Nullable String description) throws Exception;

  List<DesignParadigm> getAllParadigms() throws Exception;

  Optional<DesignParadigm> getParadigmById(int id) throws Exception;

  Optional<DesignParadigm> getParadigmByName(String name) throws Exception;

  default boolean containsParadigmWithId(int id) throws Exception {
    return getParadigmById(id).isPresent();
  }

  default boolean containsParadigmWithName(String name) throws Exception {
    return getParadigmByName(name).isPresent();
  }

  void merge(DesignParadigm paradigm) throws Exception;

  void setName(int id, String name) throws Exception;

  void setDescription(int id, @Nullable String description) throws Exception;

  void delete(DesignParadigm paradigm) throws Exception;

  void deleteByName(String name) throws Exception;

  void deleteById(int id) throws Exception;
}
