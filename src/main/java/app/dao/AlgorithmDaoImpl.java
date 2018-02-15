package app.dao;

import app.auxiliary.Connector;
import app.auxiliary.Util;
import app.model.Algorithm;
import app.model.DesignParadigm;
import app.model.FieldOfStudy;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AlgorithmDaoImpl implements AlgorithmDao {
  private static final Logger logger = LogManager.getLogger(AlgorithmDaoImpl.class);

  private Connection connection;
  private PreparedStatement insertAlgorithm;
  private PreparedStatement allAlgorithms;
  private PreparedStatement getAlgoByName;
  private PreparedStatement deleteById;

  public AlgorithmDaoImpl() throws SQLException {
    connection = Connector.getConnection();
    insertAlgorithm =
        connection.prepareStatement(
            "INSERT INTO algorithm (algorithm, complexity, algo_paradigm_id, algo_field_id) VALUE (?, ?, ?, ?)");
    String bigQuery =
        "SELECT algorithm_id, algorithm, complexity, paradigm_id, paradigm, dp.description, field_id, field, fos.description\n"
            + "FROM\n"
            + "    ((algorithm\n"
            + "    INNER JOIN design_paradigm as dp ON algo_paradigm_id = paradigm_id)\n"
            + "    INNER JOIN field_of_study as fos ON algo_field_id = field_id)";
    allAlgorithms = connection.prepareStatement(bigQuery);
    getAlgoByName = connection.prepareStatement(bigQuery + " WHERE algorithm = ?");
    deleteById = connection.prepareStatement("DELETE FROM algorithm WHERE algorithm_id = ?");
  }

  @Override
  public Algorithm insertAlgorithm(
      String name, String complexity, DesignParadigm designParadigm, FieldOfStudy fieldOfStudy)
      throws SQLException {
    insertAlgorithm.setString(1, name);
    insertAlgorithm.setString(2, complexity);
    insertAlgorithm.setInt(3, designParadigm.getId());
    insertAlgorithm.setInt(4, fieldOfStudy.getId());
    logger.debug(Util.format(insertAlgorithm));
    insertAlgorithm.executeUpdate();
    int id = Util.getLastId(connection);
    return new Algorithm(id, name, complexity, designParadigm, fieldOfStudy);
  }

  @Override
  public List<Algorithm> getAllAlgorithms() throws SQLException {
    logger.debug(Util.format(allAlgorithms));
    ResultSet result = allAlgorithms.executeQuery();
    return algorithmFromResultSet(result);
  }

  @Override
  public Optional<Algorithm> getAlgorithmByName(String name) throws SQLException {
    getAlgoByName.setString(1, name);
    logger.debug(Util.format(getAlgoByName));
    List<Algorithm> list = algorithmFromResultSet(getAlgoByName.executeQuery());
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
      whereClause += " AND paradigm_id = " + designParadigm.getId();
    }
    if (fieldOfStudy != null) {
      whereClause += " AND field_id = " + fieldOfStudy.getId();
    }
    if (whereClause.length() == 0) {
      return getAllAlgorithms();
    } else {
      Statement statement = connection.createStatement();
      String allQuery = query + whereClause;
      logger.debug(Util.format(allQuery));
      ResultSet set = statement.executeQuery(allQuery);
      return algorithmFromResultSet(set);
    }
  }

  @Override
  public void deleteById(int id) throws SQLException {
    deleteById.setInt(1, id);
    logger.debug(Util.format(deleteById));
    deleteById.executeUpdate();
  }

  @Override
  public void updateEntry(String column, String value, int id) throws SQLException {
    String query =
        "UPDATE algorithm SET "
            + column
            + " = "
            + Util.fixForString(value)
            + " WHERE algorithm_id = "
            + id;
    logger.debug(query);
    Statement statement = connection.createStatement();
    statement.executeUpdate(query);
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
