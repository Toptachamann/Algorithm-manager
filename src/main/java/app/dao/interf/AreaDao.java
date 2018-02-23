package app.dao.interf;

import app.model.AreaOfUse;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface AreaDao {
  List<AreaOfUse> getAllAreas() throws Exception;

  AreaOfUse createAreaOfUse(String area, String description) throws Exception;

  AreaOfUse createAreaOfUse(String area) throws Exception;

  Optional<AreaOfUse> getAreaById(int id) throws Exception;

  Optional<AreaOfUse> getAreaByName(String name) throws Exception;

  void deleteAreaOfUse(int id) throws Exception;

  List<AreaOfUse> getAreasOfUse(int algorithmId) throws Exception;
}
