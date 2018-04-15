package com.algorithm.manager.dao.interf;

import com.algorithm.manager.model.Algorithm;
import com.algorithm.manager.model.Application;
import com.algorithm.manager.model.AreaOfUse;

import java.util.List;
import java.util.Optional;

public interface ApplicationDao {

  void persist(Application application) throws Exception;

  default boolean containsApplication(Application application) throws Exception {
    return getApplicationOf(application.getAlgorithm(), application.getAreaOfUse()).isPresent();
  }

  Optional<Application> getApplicationOf(Algorithm algorithm, AreaOfUse areaOfUse) throws Exception;

  void deleteApplication(Application application) throws Exception;

  List<Application> getApplicationsByArea(AreaOfUse area) throws Exception;

  List<Application> getApplicationsByAlgorithm(Algorithm algorithm) throws Exception;
}
