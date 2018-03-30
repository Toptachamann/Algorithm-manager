package app.service;

import app.auxiliary.LogicException;
import app.dao.interf.AlgorithmDao;
import app.dao.interf.ApplicationDao;
import app.dao.interf.AreaDao;
import app.dao.interf.FieldDao;
import app.dao.interf.ParadigmDao;
import app.model.Algorithm;
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
    return algorithmDao.createAlgorithm(name, complexity, designParadigm, fieldOfStudy);
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
  public List<Algorithm> getAlgorithmsByArea(AreaOfUse areaOfUse) throws Exception {
    return algorithmDao.getAlgorithmsByArea(areaOfUse.getId());
  }

  @Override
  public void updateAlgorithmName(Algorithm algorithm, String name) throws Exception {
    Validate.isTrue(!StringUtils.isBlank(name));
    if (algorithmDao.getAlgorithmByName(name).isPresent()) {
      throw new LogicException("Algorithm with this name already exists");
    }
    algorithmDao.setName(algorithm, name);
  }

  @Override
  public void updateAlgorithmComplexity(Algorithm algorithm, String complexity) throws Exception {
    Validate.isTrue(!StringUtils.isBlank(complexity));
    algorithmDao.setComplexity(algorithm, complexity);
  }

  @Override
  public void setDesignParadigm(Algorithm algorithm, DesignParadigm paradigm) throws Exception {
    algorithmDao.setDesignParadigm(algorithm, paradigm);
  }

  @Override
  public void setFieldOfStudy(Algorithm algorithm, FieldOfStudy field) throws Exception {
    algorithmDao.setFieldOfStudy(algorithm, field);
  }

  @Override
  public void deleteAlgorithm(Algorithm algorithm) throws Exception {
    algorithmDao.deleteById(algorithm.getId());
  }

  @Override
  public DesignParadigm createDesignParadigm(String paradigmName) throws Exception {
    Validate.isTrue(!StringUtils.isBlank(paradigmName));
    if (paradigmDao.getParadigmByName(paradigmName).isPresent()) {
      throw new LogicException("This design paradigm already exists");
    } else {
      return paradigmDao.createParadigm(paradigmName);
    }
  }

  @Override
  public DesignParadigm createDesignParadigm(String paradigmName, String description)
      throws Exception {
    if (StringUtils.isBlank(description)) {
      return this.createDesignParadigm(paradigmName);
    } else {
      Validate.isTrue(!StringUtils.isBlank(paradigmName));
      if (paradigmDao.getParadigmByName(paradigmName).isPresent()) {
        throw new SQLException("This design paradigm already exists");
      } else {
        return paradigmDao.createParadigm(paradigmName, description);
      }
    }
  }

  @Override
  public List<DesignParadigm> getDesignParadigms() throws Exception {
    return paradigmDao.getAllDesignParadigms();
  }

  @Override
  public Optional<DesignParadigm> getParadigmByName(String name) throws Exception {
    return paradigmDao.getParadigmByName(name);
  }

  @Override
  public FieldOfStudy createFieldOfStudy(String fieldName, String fieldDescription)
      throws Exception {
    if (StringUtils.isBlank(fieldDescription)) {
      createFieldOfStudy(fieldName);
    }
    Validate.isTrue(!StringUtils.isBlank(fieldName));
    if (fieldDao.getFieldByName(fieldName).isPresent()) {
      throw new LogicException("This field of study already exists");
    } else {
      return fieldDao.createFieldOfStudy(fieldDescription, fieldDescription);
    }
  }

  @Override
  public FieldOfStudy createFieldOfStudy(String fieldName) throws Exception {
    Validate.isTrue(!StringUtils.isBlank(fieldName));
    if (fieldDao.getFieldByName(fieldName).isPresent()) {
      throw new LogicException("This field of study already exists");
    } else {
      return fieldDao.createFieldOfStudy(fieldName);
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
  public DesignParadigm updateDesignParadigm(DesignParadigm oldValue, String newParadigm)
      throws Exception {
    logger.trace("Updating design paradigm {}, new name is {}", oldValue, newParadigm);
    Validate.isTrue(!StringUtils.isBlank(newParadigm));
    paradigmDao.updateDesignParadigm(newParadigm, oldValue.getId());
    return new DesignParadigm(oldValue.getId(), newParadigm, oldValue.getDescription());
  }

  @Override
  public FieldOfStudy updateFieldName(FieldOfStudy field, String newField) throws Exception {
    Validate.isTrue(!StringUtils.isBlank(newField));
    fieldDao.updateFieldOfStudy(newField, field.getId());
    return new FieldOfStudy(field.getId(), newField, field.getDescription());
  }

  @Override
  public AreaOfUse createAreaOfUse(String area) throws Exception {
    Validate.isTrue(!StringUtils.isBlank(area));
    if (areaDao.getAreaByName(area).isPresent()) {
      throw new LogicException("There already exists an area of use with this name");
    } else {
      return areaDao.createAreaOfUse(area);
    }
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
        return areaDao.createAreaOfUse(area, description);
      }
    }
  }

  @Override
  public List<AreaOfUse> getAllAreas() throws Exception {
    return areaDao.getAllAreas();
  }

  @Override
  public Optional<AreaOfUse> getAreaOfUse(String name) throws Exception {
    return areaDao.getAreaByName(name);
  }

  @Override
  public List<AreaOfUse> getAreasByAlgorithm(Algorithm algorithm) throws Exception {
    return areaDao.getAreasOfUse(algorithm.getId());
  }

  @Override
  public void deleteAreaOfUse(AreaOfUse areaOfUse) throws Exception {
    areaDao.deleteAreaOfUse(areaOfUse.getId());
  }

  @Override
  public void createApplication(Algorithm algorithm, AreaOfUse areaOfUse) throws Exception {
    if (applicationDao.containsApplication(algorithm, areaOfUse)) {
      throw new LogicException("This algorithm application does already exist");
    } else {
      applicationDao.createApplication(algorithm, areaOfUse);
    }
  }

  @Override
  public void deleteApplication(Algorithm algorithm, AreaOfUse areaOfUse) throws Exception {
    if (!applicationDao.containsApplication(algorithm, areaOfUse)) {
      throw new LogicException("This application didn't exist");
    } else {
      applicationDao.deleteApplication(algorithm, areaOfUse);
    }
  }
}
