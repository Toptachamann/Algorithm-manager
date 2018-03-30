package app.dao.interf;

import app.model.Algorithm;
import app.model.Application;
import app.model.AreaOfUse;

import java.util.List;
import java.util.Optional;

public interface ApplicationDao {

  Application createApplication(Algorithm algorithm, AreaOfUse areaOfUse) throws Exception;

  default boolean containsApplication(Algorithm algorithm, AreaOfUse areaOfUse) throws Exception {
    return getApplication(algorithm, areaOfUse).isPresent();
  }

  Optional<Application> getApplication(Algorithm algorithm, AreaOfUse areaOfUse) throws Exception;

  void deleteApplication(Algorithm algorithm, AreaOfUse areaOfUse) throws Exception;

  List<Application> getApplicationsByArea(AreaOfUse area) throws Exception;

  List<Application> getApplicationsByAlgorithm(Algorithm algorithm) throws Exception;
}
