package app.dao.interf;

import app.model.Algorithm;
import app.model.Application;
import app.model.AreaOfUse;

import java.sql.SQLException;
import java.util.List;

public interface ApplicationDao {

  Application createApplication(Algorithm algorithm, AreaOfUse areaOfUse) throws Exception;

  boolean containsApplication(Algorithm algorithm, AreaOfUse areaOfUse) throws Exception;

  void deleteApplication(Algorithm algorithm, AreaOfUse areaOfUse) throws Exception;

  List<Application> getApplicationsByArea(AreaOfUse area) throws Exception;
}
