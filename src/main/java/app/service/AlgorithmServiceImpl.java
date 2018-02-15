package app.service;

import app.dao.AlgorithmDao;
import app.dao.FieldDao;
import app.dao.ParadigmDao;
import app.model.Algorithm;
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

  public AlgorithmServiceImpl(
      AlgorithmDao algorithmDao, ParadigmDao paradigmDao, FieldDao fieldDao) {
    this.algorithmDao = algorithmDao;
    this.paradigmDao = paradigmDao;
    this.fieldDao = fieldDao;
  }

  @Override
  public Algorithm createAlgorithm(
      String name, String complexity, DesignParadigm designParadigm, FieldOfStudy fieldOfStudy)
      throws SQLException {
    Optional<DesignParadigm> paradigm = paradigmDao.getParadigmById(designParadigm.getId());
    Optional<FieldOfStudy> field = fieldDao.getFieldById(fieldOfStudy.getId());
    Optional<Algorithm> algorithm = algorithmDao.getAlgorithmByName(name.trim());
    if (!paradigm.isPresent()) {
      throw new SQLException("Can't find paradigm by " + designParadigm);
    } else if (!field.isPresent()) {
      throw new SQLException("Can't find field of study by " + fieldOfStudy);
    } else if (!algorithm.isPresent()) {
      throw new SQLException("Algorithm with this name is already present");
    }
    return algorithmDao.insertAlgorithm(name, complexity, designParadigm, fieldOfStudy);
  }

  @Override
  public List<Algorithm> searchAlgorithm(
      String name, String complexity, DesignParadigm designParadigm, FieldOfStudy fieldOfStudy)
      throws SQLException {
    return algorithmDao.searchAlgorithm(name, complexity, designParadigm, fieldOfStudy);
  }

  @Override
  public List<Algorithm> getAllAlgorithms() throws SQLException {
    return algorithmDao.getAllAlgorithms();
  }

  @Override
  public List<DesignParadigm> getDesignParadigms() throws SQLException {
    return paradigmDao.getAllDesignParadigms();
  }

  @Override
  public List<FieldOfStudy> getAllFieldsOfStudy() throws SQLException {
    return fieldDao.getAllFieldsOfStudy();
  }

  @Override
  public DesignParadigm insertDesignParadigm(String paradigmName, String description)
      throws SQLException {
    if (StringUtils.isBlank(description)) {
      return this.insertDesignParadigm(paradigmName);
    } else if (StringUtils.isBlank(paradigmName)) {
      throw new SQLException("Paradigm name is blank");
    } else {
      if (paradigmDao.getParadigmByName(paradigmName).isPresent()) {
        throw new SQLException("There already exists design paradigm with such name");
      } else {
        return paradigmDao.insertParadigm(paradigmName, description);
      }
    }
  }

  public DesignParadigm insertDesignParadigm(String paradigmName) throws SQLException {
    if (paradigmDao.getParadigmByName(paradigmName).isPresent()) {
      if (StringUtils.isBlank(paradigmName)) {
        throw new SQLException("Paradigm name is blank");
      }
      throw new SQLException("This design paradigm already exists");
    } else {
      return paradigmDao.insertParadigm(paradigmName);
    }
  }

  @Override
  public FieldOfStudy insertFieldOfStudy(String fieldName, String fieldDescription)
      throws SQLException {
    if (StringUtils.isBlank(fieldName)) {
      throw new SQLException("Paradigm name is blank");
    } else if (StringUtils.isBlank(fieldName)) {
      throw new SQLException("This field of study already exists");
    } else {
      return fieldDao.insertFieldOfStudy(fieldDescription, fieldDescription);
    }
  }

  @Override
  public FieldOfStudy insertFieldOfStudy(String fieldName) throws SQLException {
    if (fieldDao.getFieldByName(fieldName).isPresent()) {
      throw new SQLException("This field of study already exists");
    } else if (StringUtils.isBlank(fieldName)) {
      throw new SQLException("Paradigm name is blank");
    } else {
      return fieldDao.insertFieldOfStudy(fieldName);
    }
  }

  @Override
  public void deleteItem(Algorithm algorithm) throws SQLException {
    algorithmDao.deleteById(algorithm.getId());
  }

  @Override
  public void updateAlgorithmName(Algorithm algorithm, String newName) throws SQLException {
    Validate.isTrue(!StringUtils.isBlank(newName));
    algorithmDao.updateEntry("algorithm", newName.trim(), algorithm.getId());
  }

  @Override
  public void updateAlgorithmComplexity(Algorithm algorithm, String newComplexity)
      throws SQLException {
    Validate.isTrue(!StringUtils.isBlank(newComplexity));
    algorithmDao.updateEntry("complexity", newComplexity.trim(), algorithm.getId());
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
  public FieldOfStudy updateFieldOfStudy(FieldOfStudy field, String newField) throws SQLException {
    Validate.isTrue(!StringUtils.isBlank(newField));
    fieldDao.updateFieldOfStudy(newField, field.getId());
    return new FieldOfStudy(field.getId(), newField, field.getDescription());
  }
}
