package com.algorithm.manager.dao.interf;

import com.algorithm.manager.model.FieldOfStudy;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public interface FieldDao {
  int persist(FieldOfStudy fieldOfStudy) throws Exception;

  default FieldOfStudy persist(String name, @Nullable String description) throws Exception {
    FieldOfStudy fieldOfStudy = new FieldOfStudy(name, description);
    persist(fieldOfStudy);
    return fieldOfStudy;
  }

  default FieldOfStudy persist(String name) throws Exception {
    return persist(name, null);
  }

  void refresh(FieldOfStudy fieldOfStudy) throws Exception;

  List<FieldOfStudy> getFieldsOfStudy() throws Exception;

  Optional<FieldOfStudy> getFieldOfStudyByName(String name) throws Exception;

  Optional<FieldOfStudy> getFieldOfStudyById(int id) throws Exception;

  default boolean containsFieldOfStudyWithId(int id) throws Exception {
    return getFieldOfStudyById(id).isPresent();
  }

  default boolean containsFieldOfStudyWithName(String name) throws Exception {
    return getFieldOfStudyByName(name).isPresent();
  }

  void merge(FieldOfStudy fieldOfStudy) throws Exception;

  void setName(int id, String name) throws Exception;

  default void setName(FieldOfStudy fieldOfStudy, String name) throws Exception {
    setName(fieldOfStudy.getId(), name);
    fieldOfStudy.setName(name);
  }

  void setDescription(int id, String description) throws Exception;

  default void setDescription(FieldOfStudy fieldOfStudy, @Nullable String description)
      throws Exception {
    setDescription(fieldOfStudy.getId(), description);
    fieldOfStudy.setDescription(description);
  }

  void delete(FieldOfStudy fieldOfStudy) throws Exception;

  int deleteByName(String name) throws Exception;

  int deleteById(int id) throws Exception;
}
