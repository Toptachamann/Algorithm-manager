package app.dao.interf;

import app.model.AreaOfUse;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface AreaDao {
  List<AreaOfUse> getAllAreas() throws SQLException;

  AreaOfUse createAreaOfUse(String area, String description) throws SQLException;

  AreaOfUse createAreaOfUse(String area) throws SQLException;

  Optional<AreaOfUse> getAreaById(int id) throws SQLException;

  Optional<AreaOfUse> getAreaByName(String name) throws SQLException;

  void deleteAreaOfUse(int id) throws SQLException;

  List<AreaOfUse> getAreasOfUse(int algorithmId) throws SQLException;
}
