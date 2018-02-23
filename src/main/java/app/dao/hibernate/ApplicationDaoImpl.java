package app.dao.hibernate;

import app.dao.interf.ApplicationDao;
import app.model.Algorithm;
import app.model.Application;
import app.model.AreaOfUse;

import java.sql.SQLException;

public class ApplicationDaoImpl  extends AbstractDao implements ApplicationDao{
  @Override
  public Application createApplication(Algorithm algorithm, AreaOfUse areaOfUse) throws SQLException {
    return null;
  }

  @Override
  public boolean containsApplication(Algorithm algorithm, AreaOfUse areaOfUse) throws SQLException {
    return false;
  }

  @Override
  public void deleteApplication(Algorithm algorithm, AreaOfUse areaOfUse) throws SQLException {

  }
}
