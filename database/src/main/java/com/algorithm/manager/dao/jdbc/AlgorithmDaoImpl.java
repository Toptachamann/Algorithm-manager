package com.algorithm.manager.dao.jdbc;

import com.algorithm.manager.auxiliary.Util;
import com.algorithm.manager.dao.interf.AlgorithmDao;
import com.algorithm.manager.model.Algorithm;
import com.algorithm.manager.model.DesignParadigm;
import com.algorithm.manager.model.FieldOfStudy;
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
  private PreparedStatement merge;
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
    deleteById = connection.prepareStatement("DELETE FROM algorithm WHERE algorithm_id = ?");
    merge =
        connection.prepareStatement(
            "UPDATE algorithm "
                + "SET algorithm = ?, complexity = ?, algo_paradigm_id = ?, algo_field_id = ? "
                + "WHERE algorithms.algorithm.algorithm_id = ?");
  }

  @Override
  public void persist(Algorithm algorithm) throws SQLException {
    try {
      createAlgorithm.setString(1, algorithm.getName());
      createAlgorithm.setString(2, algorithm.getComplexity());
      createAlgorithm.setInt(3, algorithm.getDesignParadigm().getId());
      createAlgorithm.setInt(4, algorithm.getFieldOfStudy().getId());
      logger.debug(() -> Util.format(createAlgorithm));
      createAlgorithm.executeUpdate();
      algorithm.setId(getLastId(connection));
      connection.commit();
    } catch (SQLException e) {
      logger.catching(Level.ERROR, e);
      logger.error("Failed to persist an algorithm {}", algorithm);
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
      whereClause += " AND algorithm LIKE " + algorithm;
    }
    if (!StringUtils.isBlank(complexity)) {
      complexity = Util.fixForLike(complexity);
      whereClause += " AND complexity LIKE " + complexity;
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
  public void merge(Algorithm algorithm) throws SQLException {
    try {
      merge.setString(1, algorithm.getName());
      merge.setString(2, algorithm.getComplexity());
      merge.setInt(3, algorithm.getDesignParadigm().getId());
      merge.setInt(4, algorithm.getFieldOfStudy().getId());
      merge.setInt(5, algorithm.getId());
      logger.debug(() -> Util.format(merge));
      merge.executeUpdate();
      connection.commit();
    } catch (SQLException e) {
      logger.catching(Level.ERROR, e);
      logger.error("Failed to merge {}", algorithm);
      rollBack(connection);
      throw e;
    }
  }

  @Override
  public void delete(Algorithm algorithm) throws SQLException {
    try {
      deleteById.setInt(1, algorithm.getId());
      logger.debug(() -> Util.format(deleteById));
      deleteById.executeUpdate();
      connection.commit();
    } catch (SQLException e) {
      logger.catching(Level.ERROR, e);
      logger.error("Failed to delete an algorithm {}", algorithm);
      connection.rollback();
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
