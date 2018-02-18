package app.service;

import app.auxiliary.LogicException;
import app.dao.AlgorithmDao;
import app.dao.AreaDao;
import app.dao.FieldDao;
import app.dao.ParadigmDao;
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

  public AlgorithmServiceImpl(
      AlgorithmDao algorithmDao, ParadigmDao paradigmDao, FieldDao fieldDao, AreaDao areaDao) {
    this.algorithmDao = algorithmDao;
    this.paradigmDao = paradigmDao;
    this.fieldDao = fieldDao;
    this.areaDao = areaDao;
  }

  @Override
  public Algorithm createAlgorithm(
      String name, String complexity, DesignParadigm designParadigm, FieldOfStudy fieldOfStudy)
      throws SQLException, LogicException {
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
    return algorithmDao.insertAlgorithm(name, complexity, designParadigm, fieldOfStudy);
  }

  @Override
  public List<Algorithm> getAllAlgorithms() throws SQLException {
    return algorithmDao.getAllAlgorithms();
  }

  @Override
  public List<Algorithm> searchAlgorithm(
      String name, String complexity, DesignParadigm designParadigm, FieldOfStudy fieldOfStudy)
      throws SQLException {
    return algorithmDao.searchAlgorithm(name, complexity, designParadigm, fieldOfStudy);
  }

  @Override
  public List<Algorithm> getAlgorithmsByArea(AreaOfUse areaOfUse) throws SQLException {
    return algorithmDao.getAlgorithmsByArea(areaOfUse.getId());
  }

  @Override
  public void updateAlgorithmName(Algorithm algorithm, String newName)
      throws SQLException, LogicException {
    Validate.isTrue(!StringUtils.isBlank(newName));
    if (algorithmDao.getAlgorithmByName(newName).isPresent()) {
      throw new LogicException("Algorithm with this name already exists");
    }
    algorithmDao.updateEntry("algorithm", newName.trim(), algorithm.getId());
  }

  @Override
  public void updateAlgorithmComplexity(Algorithm algorithm, String newComplexity)
      throws SQLException {
    Validate.isTrue(!StringUtils.isBlank(newComplexity));
    algorithmDao.updateEntry("complexity", newComplexity.trim(), algorithm.getId());
  }

  @Override
  public void setDesignParadigm(Algorithm algorithm, DesignParadigm paradigm) throws SQLException {
    algorithmDao.setDesignParadigm(paradigm.getId(), algorithm.getId());
  }

  @Override
  public void setFieldOfStudy(Algorithm algorithm, FieldOfStudy field) throws SQLException {
    algorithmDao.setFieldOfStudy(field.getId(), algorithm.getId());
  }

  @Override
  public void deleteAlgorithm(Algorithm algorithm) throws SQLException {
    algorithmDao.deleteById(algorithm.getId());
  }

  @Override
  public DesignParadigm createDesignParadigm(String paradigmName)
      throws SQLException, LogicException {
    Validate.isTrue(!StringUtils.isBlank(paradigmName));
    if (paradigmDao.getParadigmByName(paradigmName).isPresent()) {
      throw new LogicException("This design paradigm already exists");
    } else {
      return paradigmDao.insertParadigm(paradigmName);
    }
  }

  @Override
  public DesignParadigm createDesignParadigm(String paradigmName, String description)
      throws SQLException, LogicException {
    if (StringUtils.isBlank(description)) {
      return this.createDesignParadigm(paradigmName);
    } else {
      Validate.isTrue(!StringUtils.isBlank(paradigmName));
      if (paradigmDao.getParadigmByName(paradigmName).isPresent()) {
        throw new SQLException("This design paradigm already exists");
      } else {
        return paradigmDao.insertParadigm(paradigmName, description);
      }
    }
  }

  @Override
  public List<DesignParadigm> getDesignParadigms() throws SQLException {
    return paradigmDao.getAllDesignParadigms();
  }

  @Override
  public Optional<DesignParadigm> getParadigmByName(String name) throws SQLException {
    return paradigmDao.getParadigmByName(name);
  }

  @Override
  public FieldOfStudy createFieldOfStudy(String fieldName, String fieldDescription)
      throws SQLException, LogicException {
    if (StringUtils.isBlank(fieldDescription)) {
      createFieldOfStudy(fieldName);
    }
    Validate.isTrue(!StringUtils.isBlank(fieldName));
    if (fieldDao.getFieldByName(fieldName).isPresent()) {
      throw new LogicException("This field of study already exists");
    } else {
      return fieldDao.insertFieldOfStudy(fieldDescription, fieldDescription);
    }
  }

  @Override
  public FieldOfStudy createFieldOfStudy(String fieldName) throws SQLException, LogicException {
    Validate.isTrue(!StringUtils.isBlank(fieldName));
    if (fieldDao.getFieldByName(fieldName).isPresent()) {
      throw new LogicException("This field of study already exists");
    } else {
      return fieldDao.insertFieldOfStudy(fieldName);
    }
  }

  @Override
  public List<FieldOfStudy> getAllFieldsOfStudy() throws SQLException {
    return fieldDao.getAllFieldsOfStudy();
  }

  @Override
  public Optional<FieldOfStudy> getFieldByName(String name) throws SQLException {
    return fieldDao.getFieldByName(name);
  }

  @Override
  public DesignParadigm updateDesignParadigm(DesignParadigm oldValue, String newParadigm)
      throws SQLException {
    logger.trace("Updating design paradigm {}, new name is {}", oldValue, newParadigm);
    Validate.isTrue(!StringUtils.isBlank(newParadigm));
    paradigmDao.updateDesignParadigm(newParadigm, oldValue.getId());
    return new DesignParadigm(oldValue.getId(), newParadigm, oldValue.getDescription());
  }

  @Override
  public FieldOfStudy updateFieldName(FieldOfStudy field, String newField) throws SQLException {
    Validate.isTrue(!StringUtils.isBlank(newField));
    fieldDao.updateFieldOfStudy(newField, field.getId());
    return new FieldOfStudy(field.getId(), newField, field.getDescription());
  }

  @Override
  public AreaOfUse createAreaOfUse(String area) throws SQLException, LogicException {
    Validate.isTrue(!StringUtils.isBlank(area));
    if (areaDao.getAreaByName(area).isPresent()) {
      throw new LogicException("There already exists an area of use with this name");
    } else {
      return areaDao.createAreaOfUse(area);
    }
  }

  @Override
  public AreaOfUse createAreaOfUse(String area, String description)
      throws SQLException, LogicException {
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
  public List<AreaOfUse> getAllAreas() throws SQLException {
    return areaDao.getAllAreas();
  }

  @Override
  public Optional<AreaOfUse> getAreaOfUse(String name) throws SQLException {
    return areaDao.getAreaByName(name);
  }

  @Override
  public List<AreaOfUse> getAreasByAlgorithm(Algorithm algorithm) throws SQLException {
    return areaDao.getAreasOfUse(algorithm.getId());
  }

  @Override
  public void deleteAreaOfUse(AreaOfUse areaOfUse) throws SQLException {
    areaDao.deleteAreaOfUse(areaOfUse.getId());
  }

  @Override
  public void createApplication(Algorithm algorithm, AreaOfUse areaOfUse)
      throws SQLException, LogicException {
    if (areaDao.containsApplication(algorithm.getId(), areaOfUse.getId())) {
      throw new LogicException("This algorithm application does already exist");
    } else {
      areaDao.createApplication(algorithm.getId(), areaOfUse.getId());
    }
  }

  @Override
  public void deleteApplication(Algorithm algorithm, AreaOfUse areaOfUse)
      throws SQLException, LogicException {
    if (!areaDao.containsApplication(algorithm.getId(), areaOfUse.getId())) {
      throw new LogicException("This application didn't exist");
    } else {
      areaDao.deleteApplication(algorithm.getId(), areaOfUse.getId());
    }
  }
}
