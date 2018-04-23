package com.algorithm.manager.dao.interf;

import com.algorithm.manager.model.Algorithm;
import com.algorithm.manager.model.Application;
import com.algorithm.manager.model.AreaOfUse;

import java.util.List;

public interface ApplicationDao {

  int persist(Application application) throws Exception;

  default boolean containsApplication(Algorithm algorithm, AreaOfUse areaOfUse) throws Exception {
    return !getApplicationsOf(algorithm, areaOfUse).isEmpty();
  }

  List<Application> getApplicationsOf(Algorithm algorithm, AreaOfUse areaOfUse) throws Exception;

  List<Application> getApplicationsByArea(AreaOfUse area) throws Exception;

  List<Application> getApplicationsByAlgorithm(Algorithm algorithm) throws Exception;

  void delete(Application application);

  void deleteApplications(Algorithm algorithm, AreaOfUse areaOfUse);

  void deleteById(int id) throws Exception;
}
