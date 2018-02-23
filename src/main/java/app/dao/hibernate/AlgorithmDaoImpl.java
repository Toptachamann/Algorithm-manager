package app.dao.hibernate;

import app.dao.interf.AlgorithmDao;
import app.model.Algorithm;
import app.model.DesignParadigm;
import app.model.FieldOfStudy;

import javax.persistence.EntityManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class AlgorithmDaoImpl extends AbstractDao implements AlgorithmDao {
  @Override
  public Algorithm createAlgorithm(String name, String complexity, DesignParadigm designParadigm, FieldOfStudy fieldOfStudy) throws SQLException {
    Algorithm algorithm = new Algorithm(name, complexity, designParadigm, fieldOfStudy);
    EntityManager entityManager = getEntityManager();
    entityManager.getTransaction().begin();
    entityManager.persist(algorithm);
    entityManager.getTransaction().commit();
    entityManager.close();
    return algorithm;
  }

  @Override
  public List<Algorithm> getAllAlgorithms() throws SQLException {
    return null;
  }

  @Override
  public Optional<Algorithm> getAlgorithmByName(String name) throws SQLException {
    return Optional.empty();
  }

  @Override
  public List<Algorithm> searchAlgorithm(String algorithm, String complexity, DesignParadigm designParadigm, FieldOfStudy fieldOfStudy) throws SQLException {
    return null;
  }

  @Override
  public void setDesignParadigm(int paradigmId, int algorithmId) throws SQLException {

  }

  @Override
  public void setFieldOfStudy(int fieldId, int algorithmId) throws SQLException {

  }

  @Override
  public void updateEntry(String column, String value, int id) throws SQLException {

  }

  @Override
  public void deleteById(int id) throws SQLException {

  }

  @Override
  public List<Algorithm> getAlgorithmsByArea(int areaId) throws SQLException {
    return null;
  }
}
