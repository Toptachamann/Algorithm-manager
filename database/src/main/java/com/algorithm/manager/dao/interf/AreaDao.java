package com.algorithm.manager.dao.interf;

import com.algorithm.manager.model.AreaOfUse;

import java.util.List;
import java.util.Optional;

public interface AreaDao {
  List<AreaOfUse> getAllAreas() throws Exception;

  void persist(AreaOfUse areaOfUse) throws Exception;

  default boolean containsAreaOfUse(String name) throws Exception {
    return getAreaByName(name).isPresent();
  }

  Optional<AreaOfUse> getAreaById(int id) throws Exception;

  Optional<AreaOfUse> getAreaByName(String name) throws Exception;

  void delete(AreaOfUse areaOfUse) throws Exception;
}
