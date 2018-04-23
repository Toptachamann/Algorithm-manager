package com.algorithm.manager.dao.interf;

import com.algorithm.manager.model.AreaOfUse;

import java.util.List;
import java.util.Optional;

public interface AreaDao {
  List<AreaOfUse> getAllAreas() throws Exception;

  int persist(AreaOfUse areaOfUse) throws Exception;

  default boolean containsAreaOfUseWithName(String name) throws Exception {
    return getAreaOfUseByName(name).isPresent();
  }

  Optional<AreaOfUse> getAreaOfUseById(int id) throws Exception;

  Optional<AreaOfUse> getAreaOfUseByName(String name) throws Exception;

  void deleteById(int id) throws Exception;

  void deleteByName(String name) throws Exception;

  void delete(AreaOfUse areaOfUse) throws Exception;
}
