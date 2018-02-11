package app.service;

import app.auxiliary.Connector;
import app.auxiliary.Util;
import app.model.Algorithm;
import app.model.DesignParadigm;
import app.model.FieldOfStudy;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class AlgorithmDao implements AlgorithmService {
  private static AlgorithmDao dao;

  private Connection connection;
  private PreparedStatement allAlgorithms;
  private PreparedStatement allParadigms;
  private PreparedStatement allFieldsOfStudy;
  private PreparedStatement insertParadigmByName;
  private PreparedStatement insertParadigm;
  private PreparedStatement insertFieldByName;
  private PreparedStatement insertField;
  private PreparedStatement insertAlgorithm;
  private PreparedStatement getAlgoByName;

  private AlgorithmDao() throws SQLException {
    connection = Connector.getConnection();
    String bigQuery =
        "SELECT algorithm_id, algorithm, complexity, paradigm, field "
            + "FROM ((algorithm INNER JOIN design_paradigm ON algo_paradigm_id = paradigm_id) "
            + "INNER JOIN field_of_study ON algo_field_id = field_id)";
    allAlgorithms = connection.prepareStatement(bigQuery);
    allParadigms = connection.prepareStatement("SELECT * FROM design_paradigm");
    allFieldsOfStudy = connection.prepareStatement("SELECT * FROM field_of_study");
    insertParadigmByName =
        connection.prepareStatement("INSERT INTO design_paradigm (paradigm) VALUE (?)");
    insertParadigm =
        connection.prepareStatement(
            "INSERT INTO design_paradigm (paradigm, description) VALUE (?, ?)");
    insertFieldByName = connection.prepareStatement("INSERT INTO field_of_study (field) VALUE (?)");
    insertField =
        connection.prepareStatement("INSERT INTO field_of_study (field, description) VALUE (?, ?)");
    insertAlgorithm =
        connection.prepareStatement(
            "INSERT INTO algorithm (algorithm, complexity, algo_paradigm_id, algo_field_id) VALUE (?, ?, ?, ?)");
    getAlgoByName = connection.prepareStatement(bigQuery + " WHERE algorithm = ?");
  }

  public static AlgorithmDao getDao() throws SQLException {
    if (dao == null) {
      dao = new AlgorithmDao();
    }
    return dao;
  }

  @Override
  public List<Algorithm> getAlgorithms() throws SQLException {
    ResultSet result = allAlgorithms.executeQuery();
    return algorithmFromResultSet(result);
  }

  @Override
  public Algorithm getById(int id) {
    return null;
  }

  @Override
  public Algorithm getAlgorithmByName(String name) throws SQLException {
    getAlgoByName.setString(1, name);
    ResultSet set = getAlgoByName.executeQuery();
    if (set.next()) {
      return new Algorithm(
          set.getInt(1), set.getString(2), set.getString(3), set.getString(4), set.getString(5));
    } else {
      return null;
    }
  }

  @Override
  public <T> void deleteItem(String table, String keyColumn, T id) throws SQLException {
    String query = "DELETE FROM " + table + " WHERE " + keyColumn + " = ";
    if (id instanceof Integer) {
      query += id;
    } else {
      query += Util.fixForString(id.toString());
    }
    System.out.println(query);
    Statement statement = connection.createStatement();
    statement.executeUpdate(query);
  }

  @Override
  public <T> void updateEntry(
      String table, String column, String value, String primaryKeyColumn, T id)
      throws SQLException {
    String query =
        "UPDATE "
            + table
            + " Set "
            + column
            + " = "
            + Util.fixForString(value)
            + " WHERE "
            + primaryKeyColumn
            + " = ";
    if (id instanceof Integer) {
      query += id;
    } else {
      query += Util.fixForString(id.toString());
    }
    Statement statement = connection.createStatement();
    statement.executeUpdate(query);
  }

  @Override
  public List<Algorithm> searchAlgorithm(
      String algorithm, String complexity, String designParadigm, String fieldOfStudy)
      throws SQLException {
    String query =
        "SELECT \n"
            + "    algorithm_id, algorithm, complexity, paradigm, field\n"
            + "FROM\n"
            + "    ((algorithm\n"
            + "    INNER JOIN design_paradigm ON algo_paradigm_id = paradigm_id)\n"
            + "    INNER JOIN field_of_study ON algo_field_id = field_id)\n"
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
    if (!StringUtils.isBlank(designParadigm)) {
      designParadigm = Util.fixForLike(designParadigm);
      whereClause += " AND paradigm LIKE " + designParadigm.trim();
    }
    if (!StringUtils.isBlank(fieldOfStudy)) {
      fieldOfStudy = Util.fixForLike(fieldOfStudy);
      whereClause += " AND field LIKE " + fieldOfStudy.trim();
    }
    if (whereClause.length() == 0) {
      return getAlgorithms();
    } else {
      Statement statement = connection.createStatement();
      System.out.println(query + whereClause);
      ResultSet set = statement.executeQuery(query + whereClause);
      return algorithmFromResultSet(set);
    }
  }


  private List<Algorithm> algorithmFromResultSet(ResultSet set) throws SQLException {
    List<Algorithm> algorithms = new ArrayList<>();
    while (set.next()) {
      algorithms.add(
          new Algorithm(
              set.getInt(1),
              set.getString(2),
              set.getString(3),
              set.getString(4),
              set.getString(5)));
    }
    return algorithms;
  }

  @Override
  public List<DesignParadigm> getDesignParadigms() throws SQLException {
    ResultSet result = allParadigms.executeQuery();
    List<DesignParadigm> paradigms = new ArrayList<>();
    while (result.next()) {
      paradigms.add(new DesignParadigm(result.getInt(1), result.getString(2), result.getString(3)));
    }
    return paradigms;
  }

  @Override
  public List<FieldOfStudy> getFieldsOfStudy() throws SQLException {
    ResultSet result = allFieldsOfStudy.executeQuery();
    List<FieldOfStudy> fields = new ArrayList<>();
    while (result.next()) {
      fields.add(new FieldOfStudy(result.getInt(1), result.getString(2), result.getString(3)));
    }
    return fields;
  }

  @Override
  public Algorithm insertAlgorithm(
      String algoName, String algoComplexity, String designParadigm, String fieldOfStudy)
      throws SQLException {
    DesignParadigm paradigm = getParadigmByName(designParadigm);
    FieldOfStudy field = getFieldByName(fieldOfStudy);
    if (paradigm == null) {
      throw new SQLException("Can't find paradigm by " + designParadigm);
    } else if (field == null) {
      throw new SQLException("Can't find field of study by " + fieldOfStudy);
    }
    insertAlgorithm.setString(1, algoName);
    insertAlgorithm.setString(2, algoComplexity);
    insertAlgorithm.setInt(3, paradigm.getId());
    insertAlgorithm.setInt(4, field.getId());
    insertAlgorithm.executeUpdate();
    int id = Util.getLastId(connection);
    return new Algorithm(id, algoName, algoComplexity, designParadigm, fieldOfStudy);
  }

  @Override
  public void insertParadigm(String paradigm) throws SQLException {
    insertParadigmByName.setString(1, paradigm);
    insertParadigmByName.executeUpdate();
  }

  @Override
  public void insertParadigm(String paradigm, String description) throws SQLException {
    insertParadigm.setString(1, paradigm);
    insertParadigm.setString(2, description);
    insertParadigm.executeUpdate();
  }

  @Override
  public void insertFieldOfStudy(String fieldOfStudy) throws SQLException {
    insertFieldByName.setString(1, fieldOfStudy);
    insertFieldByName.executeUpdate();
  }

  @Override
  public void insertFieldOfStudy(String fieldOfStudy, String description) throws SQLException {
    insertField.setString(1, fieldOfStudy);
    insertField.setString(2, description);
    insertField.executeUpdate();
  }

  @Override
  @Nullable
  public DesignParadigm getParadigmByName(String name) throws SQLException {
    Statement query = connection.createStatement();
    ResultSet set =
        query.executeQuery("SELECT * FROM design_paradigm WHERE paradigm = '" + name + "'");
    if (set.next()) {
      return new DesignParadigm(set.getInt(1), set.getString(2), set.getString(3));
    } else {
      return null;
    }
  }

  @Override
  public FieldOfStudy getFieldByName(String name) throws SQLException {
    Statement query = connection.createStatement();
    ResultSet set = query.executeQuery("SELECT * FROM field_of_study WHERE field = '" + name + "'");
    if (set.next()) {
      return new FieldOfStudy(set.getInt(1), set.getString(2), set.getString(3));
    } else {
      return null;
    }
  }


}
