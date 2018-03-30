package app.dao.jdbc;

import app.auxiliary.Util;
import app.dao.interf.AlgorithmDao;
import app.model.Algorithm;
import app.model.AreaOfUse;
import app.model.DesignParadigm;
import app.model.FieldOfStudy;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AlgorithmDaoImpl extends AbstractDao implements AlgorithmDao {
  private static final Logger logger = LogManager.getLogger(AlgorithmDaoImpl.class);

  private PreparedStatement createAlgorithm;
  private PreparedStatement allAlgorithms;
  private PreparedStatement getAlgorithmByName;
  private PreparedStatement getAlgorithmsByArea;
  private PreparedStatement setName;
  private PreparedStatement setComplexity;
  private PreparedStatement setParadigm;
  private PreparedStatement setField;
  private PreparedStatement deleteById;

  public AlgorithmDaoImpl() throws SQLException {
    createAlgorithm =
        connection.prepareStatement(
            "INSERT INTO algorithm (algorithm, complexity, algo_paradigm_id, algo_field_id) VALUE (?, ?, ?, ?)");
    String bigQuery =
        "SELECT algorithm_id, algorithm, complexity, paradigm_id, paradigm, dp.description, field_id, field, fos.description\n"
            + "FROM\n"
            + "    ((algorithm\n"
            + "    INNER JOIN design_paradigm as dp ON algo_paradigm_id = paradigm_id)\n"
            + "    INNER JOIN field_of_study as fos ON algo_field_id = field_id)";
    allAlgorithms = connection.prepareStatement(bigQuery);
    getAlgorithmByName = connection.prepareStatement(bigQuery + " WHERE algorithm = ?");
    getAlgorithmsByArea =

    deleteById = connection.prepareStatement("DELETE FROM algorithm WHERE algorithm_id = ?");
    setParadigm =
        connection.prepareStatement(
            "UPDATE algorithms.algorithm "
                + "SET algorithms.algorithm.algo_paradigm_id = ? WHERE algorithms.algorithm.algorithm_id = ?");
    setField =
        connection.prepareStatement(
            "UPDATE algorithms.algorithm "
                + "SET algorithms.algorithm.algo_field_id = ? WHERE algorithms.algorithm.algorithm_id = ?");
    setName =
        connection.prepareStatement(
            "UPDATE  algorithms.algorithm SET algorithms.algorithm.algorithm = ? "
                + "WHERE algorithms.algorithm.algorithm_id = ?");
    setComplexity =
        connection.prepareStatement(
            "UPDATE  algorithms.algorithm SET algorithms.algorithm.complexity = ? "
                + "WHERE algorithms.algorithm.algorithm_id = ?");
  }

  @Override
  public Algorithm createAlgorithm(
      String name, String complexity, DesignParadigm designParadigm, FieldOfStudy fieldOfStudy)
      throws SQLException {
    try {
      createAlgorithm.setString(1, name);
      createAlgorithm.setString(2, complexity);
      createAlgorithm.setInt(3, designParadigm.getId());
      createAlgorithm.setInt(4, fieldOfStudy.getId());
      logger.debug(() -> Util.format(createAlgorithm));
      createAlgorithm.executeUpdate();
      int id = getLastId(connection);
      connection.commit();
      return new Algorithm(id, name, complexity, designParadigm, fieldOfStudy);
    } catch (SQLException e) {
      logger.catching(Level.ERROR, e);
      logger.error("Failed to insert an algorithm {}", name);
      super.rollBack(connection);
      throw e;
    }
  }

  @Override
  public List<Algorithm> getAllAlgorithms() throws SQLException {
    logger.debug(() -> Util.format(allAlgorithms));
    ResultSet result = allAlgorithms.executeQuery();
    return algorithmFromResultSet(result);
  }

  @Override
  public Optional<Algorithm> getAlgorithmByName(String name) throws SQLException {
    getAlgorithmByName.setString(1, name);
    logger.debug(() -> Util.format(getAlgorithmByName));
    List<Algorithm> list = algorithmFromResultSet(getAlgorithmByName.executeQuery());
    if (list.size() == 1) {
      return Optional.of(list.get(0));
    } else {
      return Optional.empty();
    }
  }

  @Override
  public List<Algorithm> searchAlgorithm(
      String algorithm, String complexity, DesignParadigm designParadigm, FieldOfStudy fieldOfStudy)
      throws SQLException {
    String query =
        "SELECT \n"
            + "    algorithm_id, algorithm, complexity, paradigm_id, paradigm, dp.description, field_id, field, fos.description\n"
            + "FROM\n"
            + "    ((algorithm\n"
            + "    INNER JOIN design_paradigm as dp ON algo_paradigm_id = paradigm_id)\n"
            + "    INNER JOIN field_of_study as fos ON algo_field_id = field_id)\n"
            + "WHERE 1 = 1";

    String whereClause = "";
    if (!StringUtils.isBlank(algorithm)) {
      algorithm = Util.fixForLike(algorithm);
      whereClause += " AND algorithm LIKE " + algorithm.trim();
    }
    if (!StringUtils.isBlank(complexity)) {
      complexity = Util.fixForLike(complexity);
      whereClause += " AND complexity LIKE " + complexity.trim();
    }
    if (designParadigm != null) {
      whereClause += " AND paradigm_id = " + designParadigm.getParadigm();
    }
    if (fieldOfStudy != null) {
      whereClause += " AND field_id = " + fieldOfStudy.getField();
    }
    if (whereClause.length() == 0) {
      return getAllAlgorithms();
    } else {
      Statement statement = connection.createStatement();
      String allQuery = query + whereClause;
      logger.debug(() -> Util.format(allQuery));
      ResultSet set = statement.executeQuery(allQuery);
      return algorithmFromResultSet(set);
    }
  }

  @Override
  public void deleteById(int id) throws SQLException {
    try {
      deleteById.setInt(1, id);
      logger.debug(() -> Util.format(deleteById));
      deleteById.executeUpdate();
      connection.commit();
    } catch (SQLException e) {
      logger.catching(Level.ERROR, e);
      logger.error("Failed to delete an algorithm by id {}", id);
      connection.rollback();
      throw e;
    }
  }

  @Override
  public void setName(Algorithm algorithm, String name) throws SQLException {
    try {
      setName.setString(1, name);
      setName.setInt(2, algorithm.getId());
      logger.debug(() -> Util.format(setName));
      setName.executeUpdate();
      connection.commit();
    } catch (SQLException e) {
      logger.catching(Level.ERROR, e);
      logger.error("Failed to update name of the algorithm {} to {}", algorithm, name);
      rollBack(connection);
      throw e;
    }
  }

  @Override
  public void setComplexity(Algorithm algorithm, String complexity) throws SQLException {
    try {
      setComplexity.setString(1, complexity);
      setComplexity.setInt(2, algorithm.getId());
      logger.debug(() -> Util.format(setComplexity));
      setComplexity.executeUpdate();
      connection.commit();
    } catch (SQLException e) {
      logger.catching(Level.ERROR, e);
      logger.error("Failed to update complexity of the algorithm {} to {}", algorithm, complexity);
      rollBack(connection);
      throw e;
    }
  }

  @Override
  public void setDesignParadigm(Algorithm algorithm, DesignParadigm paradigm) throws SQLException {
    try {
      setParadigm.setInt(1, paradigm.getId());
      setParadigm.setInt(2, algorithm.getId());
      logger.debug(() -> Util.format(setParadigm));
      setParadigm.executeUpdate();
      connection.commit();
    } catch (SQLException e) {
      logger.catching(Level.ERROR, e);
      logger.error("Failed to set design paradigm {} to algorithm {}", paradigm, algorithm);
      rollBack(connection);
      throw e;
    }
  }

  @Override
  public void setFieldOfStudy(Algorithm algorithm, FieldOfStudy fieldOfStudy) throws SQLException {
    try {
      setField.setInt(2, algorithm.getId());
      setField.setInt(1, fieldOfStudy.getId());
      logger.debug(() -> Util.format(setField));
      setField.executeUpdate();
      connection.commit();
    } catch (SQLException e) {
      logger.catching(Level.ERROR, e);
      logger.error("Failed to set field of study {} to algorithm {}", fieldOfStudy, algorithm);
      rollBack(connection);
      throw e;
    }
  }

  private List<Algorithm> algorithmFromResultSet(ResultSet set) throws SQLException {
    List<Algorithm> algorithms = new ArrayList<>();
    while (set.next()) {
      DesignParadigm designParadigm =
          new DesignParadigm(set.getInt(4), set.getString(5), set.getString(6));
      FieldOfStudy fieldOfStudy =
          new FieldOfStudy(set.getInt(7), set.getString(8), set.getString(9));
      algorithms.add(
          new Algorithm(
              set.getInt(1), set.getString(2), set.getString(3), designParadigm, fieldOfStudy));
    }
    return algorithms;
  }
}
