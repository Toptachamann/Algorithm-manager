package app.dao.interf;

import app.model.Algorithm;
import app.model.Application;
import app.model.AreaOfUse;

import java.sql.SQLException;

public interface ApplicationDao {

  Application createApplication(Algorithm algorithm, AreaOfUse areaOfUse) throws SQLException;

  boolean containsApplication(Algorithm algorithm, AreaOfUse areaOfUse) throws SQLException;

  void deleteApplication(Algorithm algorithm, AreaOfUse areaOfUse) throws SQLException;
}
