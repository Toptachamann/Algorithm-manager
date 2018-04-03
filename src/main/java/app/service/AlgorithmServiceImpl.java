package app.service;

import app.auxiliary.LogicException;
import app.dao.interf.AlgorithmDao;
import app.dao.interf.ApplicationDao;
import app.dao.interf.AreaDao;
import app.dao.interf.FieldDao;
import app.dao.interf.ParadigmDao;
import app.model.Algorithm;
import app.model.Application;
import app.model.AreaOfUse;
import app.model.DesignParadigm;
import app.model.FieldOfStudy;
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
  public Algorithm createAlgorithm(
      String name, String complexity, DesignParadigm designParadigm, FieldOfStudy fieldOfStudy)
      throws Exception {
    Optional<DesignParadigm> paradigm = paradigmDao.getParadigmById(designParadigm.getId());
    Optional<FieldOfStudy> field = fieldDao.getFieldById(fieldOfStudy.getId());
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
    return algorithmDao.getAllAlgorithms();
  }

  @Override
  public List<Algorithm> searchAlgorithm(
      String name, String complexity, DesignParadigm designParadigm, FieldOfStudy fieldOfStudy)
      throws Exception {
    return algorithmDao.searchAlgorithm(name, complexity, designParadigm, fieldOfStudy);
  }

  @Override
  public List<Application> getApplicationsByArea(AreaOfUse areaOfUse) throws Exception {
    return applicationDao.getApplicationsByArea(areaOfUse);
  }

  @Override
  public void updateAlgorithmName(Algorithm algorithm, String name) throws Exception {
    Validate.isTrue(!StringUtils.isBlank(name));
    if (algorithmDao.getAlgorithmByName(name).isPresent()) {
      throw new LogicException("Algorithm with this name already exists");
    }
    algorithm.setName(name);
    algorithmDao.merge(algorithm);
  }

  @Override
  public void updateAlgorithmComplexity(Algorithm algorithm, String complexity) throws Exception {
    Validate.isTrue(!StringUtils.isBlank(complexity));
    algorithm.setComplexity(complexity);
    algorithmDao.merge(algorithm);
  }

  @Override
  public void setDesignParadigm(Algorithm algorithm, DesignParadigm paradigm) throws Exception {
    algorithm.setDesignParadigm(paradigm);
    algorithmDao.merge(algorithm);
  }

  @Override
  public void setFieldOfStudy(Algorithm algorithm, FieldOfStudy field) throws Exception {
    algorithm.setFieldOfStudy(field);
    algorithmDao.merge(algorithm);
  }

  @Override
  public void deleteAlgorithm(Algorithm algorithm) throws Exception {
    algorithmDao.delete(algorithm);
  }

  @Override
  public DesignParadigm createDesignParadigm(String paradigmName) throws Exception {
    return createDesignParadigm(paradigmName, null);
  }

  @Override
  public DesignParadigm createDesignParadigm(String paradigmName, String description)
      throws Exception {
    Validate.isTrue(!StringUtils.isBlank(paradigmName));
    if (paradigmDao.getParadigmByParadigm(paradigmName).isPresent()) {
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
  public Optional<DesignParadigm> getParadigmByName(String name) throws Exception {
    return paradigmDao.getParadigmByParadigm(name);
  }

  @Override
  public FieldOfStudy createFieldOfStudy(String fieldName) throws Exception {
    return createFieldOfStudy(fieldName, null);
  }

  @Override
  public FieldOfStudy createFieldOfStudy(String fieldName, String fieldDescription)
      throws Exception {
    Validate.isTrue(!StringUtils.isBlank(fieldName));
    if (fieldDao.getFieldByName(fieldName).isPresent()) {
      throw new LogicException("This field of study already exists");
    } else {
      FieldOfStudy fieldOfStudy = new FieldOfStudy(fieldName, fieldDescription);
      fieldDao.persist(fieldOfStudy);
      return fieldOfStudy;
    }
  }

  @Override
  public List<FieldOfStudy> getAllFieldsOfStudy() throws Exception {
    return fieldDao.getAllFieldsOfStudy();
  }

  @Override
  public Optional<FieldOfStudy> getFieldByName(String name) throws Exception {
    return fieldDao.getFieldByName(name);
  }

  @Override
  public DesignParadigm setParadigmName(DesignParadigm paradigm, String newParadigm)
      throws Exception {
    logger.trace("Updating design paradigm {}, new name is {}", paradigm, newParadigm);
    Validate.isTrue(!StringUtils.isBlank(newParadigm));
    paradigmDao.setParadigm(paradigm, newParadigm);
    return new DesignParadigm(paradigm.getId(), newParadigm, paradigm.getDescription());
  }

  @Override
  public FieldOfStudy setFieldName(FieldOfStudy field, String newField) throws Exception {
    Validate.isTrue(!StringUtils.isBlank(newField));
    fieldDao.setField(field, newField);
    return new FieldOfStudy(field.getId(), newField, field.getDescription());
  }

  @Override
  public AreaOfUse createAreaOfUse(String area, String description) throws Exception {
    Validate.isTrue(!StringUtils.isBlank(area));
    if (StringUtils.isBlank(description)) {
      return createAreaOfUse(area);
    } else {
      if (areaDao.getAreaByName(area).isPresent()) {
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
    return areaDao.getAreaByName(name);
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
    if (applicationDao.containsApplication(application)) {
      throw new LogicException("This algorithm application does already exist");
    } else {
      applicationDao.persist(application);
    }
  }

  @Override
  public void deleteApplication(Algorithm algorithm, AreaOfUse areaOfUse) throws Exception {
    Application application = new Application(algorithm, areaOfUse);
    if (!applicationDao.containsApplication(application)) {
      throw new LogicException("This application didn't exist");
    } else {
      applicationDao.deleteApplication(application);
    }
  }
}
