package com.algorithm.manager.service;

import com.algorithm.manager.auxiliary.LogicException;
import com.algorithm.manager.dao.interf.AlgorithmDao;
import com.algorithm.manager.dao.interf.ApplicationDao;
import com.algorithm.manager.dao.interf.AreaDao;
import com.algorithm.manager.dao.interf.FieldDao;
import com.algorithm.manager.dao.interf.ParadigmDao;
import com.algorithm.manager.model.Algorithm;
import com.algorithm.manager.model.Application;
import com.algorithm.manager.model.AreaOfUse;
import com.algorithm.manager.model.DesignParadigm;
import com.algorithm.manager.model.FieldOfStudy;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class AlgorithmServiceImpl implements AlgorithmService {
  private static final Logger logger = LogManager.getLogger(AlgorithmServiceImpl.class);

  private AlgorithmDao algorithmDao;
  private ParadigmDao paradigmDao;
  private FieldDao fieldDao;
  private AreaDao areaDao;
  private ApplicationDao applicationDao;

  public AlgorithmServiceImpl(
      AlgorithmDao algorithmDao,
      ParadigmDao paradigmDao,
      FieldDao fieldDao,
      AreaDao areaDao,
      ApplicationDao applicationDao) {
    this.algorithmDao = algorithmDao;
    this.paradigmDao = paradigmDao;
    this.fieldDao = fieldDao;
    this.areaDao = areaDao;
    this.applicationDao = applicationDao;
  }

  @Override
  public Algorithm persistAlgorithm(
      String name, String complexity, DesignParadigm designParadigm, FieldOfStudy fieldOfStudy)
      throws Exception {
    Optional<DesignParadigm> paradigm = paradigmDao.getParadigmById(designParadigm.getDesignParadigmId());
    Optional<FieldOfStudy> field = fieldDao.getFieldOfStudyById(fieldOfStudy.getId());
    Optional<Algorithm> algorithm = algorithmDao.getAlgorithmByName(name.trim());
    if (!paradigm.isPresent()) {
      throw new LogicException("Can't find paradigm by " + designParadigm);
    } else if (!field.isPresent()) {
      throw new LogicException("Can't find field of study by " + fieldOfStudy);
    } else if (algorithm.isPresent()) {
      throw new LogicException("Algorithm with this name is already present");
    }
    Algorithm algorithm1 = new Algorithm(name, complexity, designParadigm, fieldOfStudy);
    algorithmDao.persist(algorithm1);
    return algorithm1;
  }

  @Override
  public List<Algorithm> getAllAlgorithms() throws Exception {
    return algorithmDao.getAlgorithms();
  }

  @Override
  public List<Algorithm> searchAlgorithm(
      String name, String complexity, DesignParadigm designParadigm, FieldOfStudy fieldOfStudy)
      throws Exception {
    return algorithmDao.searchAlgorithm(name, complexity, designParadigm, fieldOfStudy);
  }

  @Override
  public void setAlgorithmName(Algorithm algorithm, String name) throws Exception {
    Validate.isTrue(!StringUtils.isBlank(name));
    if (algorithmDao.getAlgorithmByName(name).isPresent()) {
      throw new LogicException("Algorithm with this name already exists");
    }
    algorithm.setName(name);
    algorithmDao.merge(algorithm);
  }

  @Override
  public void setAlgorithmComplexity(Algorithm algorithm, String complexity) throws Exception {
    Validate.isTrue(!StringUtils.isBlank(complexity));
    algorithm.setComplexity(complexity);
    algorithmDao.merge(algorithm);
  }

  @Override
  public void setDesignParadigm(Algorithm algorithm, DesignParadigm paradigm) throws Exception {
    Optional<DesignParadigm> optional = paradigmDao.getDesignParadigmByName(paradigm.getName());
    if (optional.isPresent()) {
      paradigm = optional.get();
    } else {
      paradigmDao.persist(paradigm);
    }
    algorithm.setDesignParadigm(paradigm);
    algorithmDao.merge(algorithm);
  }

  @Override
  public void setFieldOfStudy(Algorithm algorithm, FieldOfStudy field) throws Exception {
    Optional<FieldOfStudy> optional = fieldDao.getFieldOfStudyByName(field.getName());
    if (optional.isPresent()) {
      field = optional.get();
    } else {
      fieldDao.persist(field);
    }
    algorithm.setFieldOfStudy(field);
    algorithmDao.merge(algorithm);
  }

  @Override
  public void deleteAlgorithm(Algorithm algorithm) throws Exception {
    algorithmDao.delete(algorithm);
  }

  // ------------------------------------------------------------------------
  @Override
  public List<Application> getApplicationsByArea(AreaOfUse areaOfUse) throws Exception {
    return applicationDao.getApplicationsByArea(areaOfUse);
  }

  @Override
  public DesignParadigm createDesignParadigm(String paradigmName) throws Exception {
    return createDesignParadigm(paradigmName, null);
  }

  @Override
  public DesignParadigm createDesignParadigm(String paradigmName, String description)
      throws Exception {
    Validate.isTrue(!StringUtils.isBlank(paradigmName));
    if (paradigmDao.getDesignParadigmByName(paradigmName).isPresent()) {
      throw new SQLException("This design paradigm already exists");
    } else {
      DesignParadigm paradigm = new DesignParadigm(paradigmName, description);
      paradigmDao.persist(paradigm);
      return paradigm;
    }
  }

  @Override
  public List<DesignParadigm> getDesignParadigms() throws Exception {
    return paradigmDao.getAllParadigms();
  }

  @Override
  public Optional<DesignParadigm> getDesignParadigmByName(String name) throws Exception {
    return paradigmDao.getDesignParadigmByName(name);
  }

  @Override
  public FieldOfStudy createFieldOfStudy(String fieldName) throws Exception {
    return createFieldOfStudy(fieldName, null);
  }

  @Override
  public FieldOfStudy createFieldOfStudy(String fieldName, String fieldDescription)
      throws Exception {
    Validate.isTrue(!StringUtils.isBlank(fieldName));
    if (fieldDao.getFieldOfStudyByName(fieldName).isPresent()) {
      throw new LogicException("This field of study already exists");
    } else {
      FieldOfStudy fieldOfStudy = new FieldOfStudy(fieldName, fieldDescription);
      fieldDao.persist(fieldOfStudy);
      return fieldOfStudy;
    }
  }

  @Override
  public List<FieldOfStudy> getAllFieldsOfStudy() throws Exception {
    return fieldDao.getFieldsOfStudy();
  }

  @Override
  public Optional<FieldOfStudy> getFieldOfStudyByName(String name) throws Exception {
    return fieldDao.getFieldOfStudyByName(name);
  }

  @Override
  public AreaOfUse createAreaOfUse(String area, String description) throws Exception {
    Validate.isTrue(!StringUtils.isBlank(area));
    if (StringUtils.isBlank(description)) {
      return createAreaOfUse(area);
    } else {
      if (areaDao.getAreaOfUseByName(area).isPresent()) {
        throw new LogicException("There already exists an area of use with this name");
      } else {
        AreaOfUse areaOfUse = new AreaOfUse(area, description);
        areaDao.persist(areaOfUse);
        return areaOfUse;
      }
    }
  }

  @Override
  public List<AreaOfUse> getAllAreas() throws Exception {
    return areaDao.getAllAreas();
  }

  @Override
  public AreaOfUse createAreaOfUse(String area) throws Exception {
    return createAreaOfUse(area, null);
  }

  @Override
  public Optional<AreaOfUse> getAreaOfUse(String name) throws Exception {
    return areaDao.getAreaOfUseByName(name);
  }

  @Override
  public List<Application> getApplicationsAlgorithm(Algorithm algorithm) throws Exception {
    return applicationDao.getApplicationsByAlgorithm(algorithm);
  }

  @Override
  public void deleteAreaOfUse(AreaOfUse areaOfUse) throws Exception {
    areaDao.delete(areaOfUse);
  }

  @Override
  public void createApplication(Algorithm algorithm, AreaOfUse areaOfUse) throws Exception {
    Application application = new Application(algorithm, areaOfUse);
    if (applicationDao.containsApplication(
        application.getAlgorithm(), application.getAreaOfUse())) {
      throw new LogicException("This algorithm application does already exist");
    } else {
      applicationDao.persist(application);
    }
  }

  @Override
  public void deleteApplication(Algorithm algorithm, AreaOfUse areaOfUse) throws Exception {
    Application application = new Application(algorithm, areaOfUse);
    if (!applicationDao.containsApplication(
        application.getAlgorithm(), application.getAreaOfUse())) {
      throw new LogicException("This application didn't exist");
    } else {
      applicationDao.delete(application);
    }
  }
}
