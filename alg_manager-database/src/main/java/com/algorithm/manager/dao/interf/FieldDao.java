package com.algorithm.manager.dao.interf;

import com.algorithm.manager.model.FieldOfStudy;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public interface FieldDao {
  int persist(FieldOfStudy fieldOfStudy) throws Exception;

  FieldOfStudy persist(String name, @Nullable String description) throws Exception;

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

  void setDescription(int id, String description) throws Exception;

  void delete(FieldOfStudy fieldOfStudy) throws Exception;

  void deleteByName(String name) throws Exception;

  void deleteById(int id) throws Exception;
}
