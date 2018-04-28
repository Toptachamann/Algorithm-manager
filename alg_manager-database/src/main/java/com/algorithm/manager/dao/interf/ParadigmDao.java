package com.algorithm.manager.dao.interf;

import com.algorithm.manager.model.DesignParadigm;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public interface ParadigmDao {
  int persist(DesignParadigm paradigm) throws Exception;

  default DesignParadigm persist(String name) throws Exception {
    return persist(name, null);
  }

  void refresh(DesignParadigm paradigm) throws Exception;

  default DesignParadigm persist(String name, @Nullable String description) throws Exception {
    DesignParadigm designParadigm = new DesignParadigm(name, description);
    persist(designParadigm);
    return designParadigm;
  }

  List<DesignParadigm> getAllParadigms() throws Exception;

  Optional<DesignParadigm> getParadigmById(int id) throws Exception;

  Optional<DesignParadigm> getDesignParadigmByName(String name) throws Exception;

  default boolean containsParadigmWithId(int id) throws Exception {
    return getParadigmById(id).isPresent();
  }

  default boolean containsParadigmWithName(String name) throws Exception {
    return getDesignParadigmByName(name).isPresent();
  }

  void merge(DesignParadigm paradigm) throws Exception;

  void setName(int id, String name) throws Exception;

  void setDescription(int id, @Nullable String description) throws Exception;

  default void setName(DesignParadigm designParadigm, String name) throws Exception {
    setName(designParadigm.getDesignParadigmId(), name);
    designParadigm.setName(name);
  }

  default void setDescription(DesignParadigm designParadigm, String description) throws Exception {
    setDescription(designParadigm.getDesignParadigmId(), description);
    designParadigm.setDescription(description);
  }

  void delete(DesignParadigm paradigm) throws Exception;

  int deleteByName(String name) throws Exception;

  int deleteById(int id) throws Exception;
}
