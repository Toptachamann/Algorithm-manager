package app.dao.hibernate;

import app.dao.interf.AreaDao;
import app.model.AreaOfUse;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class AreaDaoImpl  extends AbstractDao implements AreaDao {
  @Override
  public List<AreaOfUse> getAllAreas() throws SQLException {
    return null;
  }

  @Override
  public AreaOfUse createAreaOfUse(String area, String description) throws SQLException {
    return null;
  }

  @Override
  public AreaOfUse createAreaOfUse(String area) throws SQLException {
    return null;
  }

  @Override
  public Optional<AreaOfUse> getAreaById(int id) throws SQLException {
    return Optional.empty();
  }

  @Override
  public Optional<AreaOfUse> getAreaByName(String name) throws SQLException {
    return Optional.empty();
  }

  @Override
  public void deleteAreaOfUse(int id) throws SQLException {

  }

  @Override
  public List<AreaOfUse> getAreasOfUse(int algorithmId) throws SQLException {
    return null;
  }
}
