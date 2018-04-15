package com.algorithm.manager.dao.interf;

import com.algorithm.manager.model.FieldOfStudy;

import java.util.List;
import java.util.Optional;

public interface FieldDao {
  void persist(FieldOfStudy fieldOfStudy) throws Exception;

  List<FieldOfStudy> getAllFieldsOfStudy() throws Exception;

  Optional<FieldOfStudy> getFieldByName(String name) throws Exception;

  default boolean containsFieldOfStudy(String name) throws Exception {
    return getFieldByName(name).isPresent();
  }

  Optional<FieldOfStudy> getFieldById(int id) throws Exception;

  default boolean containsFieldOfStudy(int id) throws Exception {
    return getFieldById(id).isPresent();
  }

  void setField(FieldOfStudy fieldOfStudy, String newName) throws Exception;

  void delete(FieldOfStudy fieldOfStudy) throws Exception;
}
