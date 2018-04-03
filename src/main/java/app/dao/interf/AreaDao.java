package app.dao.interf;

import app.model.AreaOfUse;

import java.util.List;
import java.util.Optional;

public interface AreaDao {
  List<AreaOfUse> getAllAreas() throws Exception;

  void persist(AreaOfUse areaOfUse) throws Exception;

  default boolean containsAreaOfUse(AreaOfUse areaOfUse) throws Exception {
    return getAreaById(areaOfUse.getId()).isPresent();
  }

  Optional<AreaOfUse> getAreaById(int id) throws Exception;

  Optional<AreaOfUse> getAreaByName(String name) throws Exception;

  void deleteAreaOfUse(AreaOfUse areaOfUse) throws Exception;
}
