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
  private PreparedStatement getAlgorithmsByArea;
  private PreparedStatement setParadigm;
  private PreparedStatement setField;
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
    getAlgorithmsByArea =
        connection.prepareStatement(
            "SELECT \n"
                + "    algorithm_id,\n"
                + "    algorithm,\n"
                + "    complexity,\n"
                + "    paradigm_id,\n"
                + "    paradigm,\n"
                + "    dp.description,\n"
                + "    field_id,\n"
                + "    field,\n"
                + "    f.description\n"
                + "FROM\n"
                + "    (SELECT \n"
                + "        app_algorithm_id\n"
                + "    FROM\n"
                + "        algorithm_application\n"
                + "    WHERE\n"
                + "        app_area_id = ?) AS algos\n"
                + "        INNER JOIN\n"
                + "    algorithm ON algos.app_algorithm_id = algorithm_id\n"
                + "        INNER JOIN\n"
                + "    design_paradigm AS dp ON algo_paradigm_id = paradigm_id\n"
                + "        INNER JOIN\n"
                + "    field_of_study AS f ON algo_field_id = field_id\n"
                + "ORDER BY algorithm_id");
    deleteById = connection.prepareStatement("DELETE FROM algorithm WHERE algorithm_id = ?");
    setParadigm =
        connection.prepareStatement(
            "UPDATE algorithms.algorithm "
                + "SET algorithms.algorithm.algo_paradigm_id = ? WHERE algorithms.algorithm.algorithm_id = ?");
    setField =
        connection.prepareStatement(
            "UPDATE algorithms.algorithm "
                + "SET algorithms.algorithm.algo_field_id = ? WHERE algorithms.algorithm.algorithm_id = ?");
  }

  @Override
  public Algorithm insertAlgorithm(
      String name, String complexity, DesignParadigm designParadigm, FieldOfStudy fieldOfStudy)
      throws SQLException {
    insertAlgorithm.setString(1, name);
    insertAlgorithm.setString(2, complexity);
    insertAlgorithm.setInt(3, designParadigm.getId());
    insertAlgorithm.setInt(4, fieldOfStudy.getId());
    logger.debug(() -> Util.format(insertAlgorithm));
    insertAlgorithm.executeUpdate();
    int id = Util.getLastId(connection);
    return new Algorithm(id, name, complexity, designParadigm, fieldOfStudy);
  }

  @Override
  public List<Algorithm> getAllAlgorithms() throws SQLException {
    logger.debug(() -> Util.format(allAlgorithms));
    ResultSet result = allAlgorithms.executeQuery();
    return algorithmFromResultSet(result);
  }

  @Override
  public Optional<Algorithm> getAlgorithmByName(String name) throws SQLException {
    getAlgoByName.setString(1, name);
    logger.debug(() -> Util.format(getAlgoByName));
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
      whereClause += " AND paradigm_id LIKE " + designParadigm.getParadigm();
    }
    if (fieldOfStudy != null) {
      whereClause += " AND field_id LIKE " + fieldOfStudy.getField();
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
    deleteById.setInt(1, id);
    logger.debug(() -> Util.format(deleteById));
    deleteById.executeUpdate();
  }

  @Override
  public void setDesignParadigm(int paradigmId, int algorithmId) throws SQLException {
    setParadigm.setInt(1, paradigmId);
    setParadigm.setInt(2, algorithmId);
    logger.debug(() -> Util.format(setParadigm));
    setParadigm.executeUpdate();
  }

  @Override
  public void setFieldOfStudy(int fieldId, int algorithmId) throws SQLException {
    setField.setInt(1, fieldId);
    setField.setInt(2, algorithmId);
    logger.debug(() -> Util.format(setField));
    setField.executeUpdate();
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
    logger.debug(() -> Util.format(query));
    Statement statement = connection.createStatement();
    statement.executeUpdate(query);
  }

  @Override
  public List<Algorithm> getAlgorithmsByArea(int areaId) throws SQLException {
    getAlgorithmsByArea.setInt(1, areaId);
    logger.debug(() -> Util.format(getAlgorithmsByArea));
    return algorithmFromResultSet(getAlgorithmsByArea.executeQuery());
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
