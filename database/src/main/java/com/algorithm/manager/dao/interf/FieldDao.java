package com.algorithm.manager.dao.interf;

import com.algorithm.manager.model.FieldOfStudy;

import java.util.List;
import java.util.Optional;

public interface FieldDao {
  void persist(FieldOfStudy fieldOfStudy) throws Exception;

  List<FieldOfStudy> getAllFieldsOfStudy() throws Exception;

  Optional<FieldOfStudy> getFieldByName(String name) throws Exception;

  Optional<FieldOfStudy> getFieldById(int id) throws Exception;

  default boolean containsFieldOfStudy(FieldOfStudy fieldOfStudy) throws Exception {
    return getFieldById(fieldOfStudy.getId()).isPresent();
  }

  default boolean containsFieldOfStudyWithName(String name) throws Exception {
    return getFieldByName(name).isPresent();
  }

  void merge(FieldOfStudy fieldOfStudy) throws Exception;

  void delete(FieldOfStudy fieldOfStudy) throws Exception;

  void deleteByField(FieldOfStudy fieldOfStudy) throws Exception;
}
