package app.dao;

import app.auxiliary.Connector;
import app.auxiliary.Util;
import app.model.FieldOfStudy;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FieldDaoImpl extends AbstractDao implements FieldDao {
  private static final Logger logger = LogManager.getLogger(FieldDaoImpl.class);

  private Connection connection;

  private PreparedStatement createFieldByName;
  private PreparedStatement createField;
  private PreparedStatement allFieldsOfStudy;
  private PreparedStatement getFieldById;
  private PreparedStatement getFieldByName;
  private PreparedStatement updateFieldOfStudy;

  public FieldDaoImpl() throws SQLException {
    connection = Connector.getConnection();
    allFieldsOfStudy = connection.prepareStatement("SELECT * FROM field_of_study");
    createFieldByName = connection.prepareStatement("INSERT INTO field_of_study (field) VALUE (?)");
    createField =
        connection.prepareStatement("INSERT INTO field_of_study (field, description) VALUE (?, ?)");
    getFieldById = connection.prepareStatement("SELECT * FROM field_of_study WHERE field_id = ?");
    getFieldByName = connection.prepareStatement("SELECT * FROM field_of_study WHERE field = ?");
    updateFieldOfStudy =
        connection.prepareStatement(
            "UPDATE algorithms.field_of_study "
                + "SET algorithms.field_of_study.field = ? WHERE algorithms.field_of_study.field_id = ?");
  }

  @Override
  public FieldOfStudy createFieldOfStudy(String fieldOfStudy) throws SQLException {
    try {
      createFieldByName.setString(1, fieldOfStudy);
      logger.debug(() -> Util.format(createFieldByName));
      createFieldByName.executeUpdate();
      int id = getLastId(connection);
      connection.commit();
      return new FieldOfStudy(id, fieldOfStudy);
    } catch (SQLException e) {
      logger.catching(Level.ERROR, e);
      logger.error("Failed to create field of study {}", fieldOfStudy);
      rollBack(connection);
      throw e;
    }
  }

  @Override
  public FieldOfStudy createFieldOfStudy(String fieldOfStudy, String description)
      throws SQLException {
    try {
      createField.setString(1, fieldOfStudy);
      createField.setString(2, description);
      logger.debug(() -> Util.format(createField));
      createField.executeUpdate();
      int id = getLastId(connection);
      connection.commit();
      return new FieldOfStudy(id, fieldOfStudy, description);
    } catch (SQLException e) {
      logger.catching(Level.ERROR, e);
      logger.error(
          "Failed to create field of study {} with description {}", fieldOfStudy, description);
      rollBack(connection);
      throw e;
    }
  }

  @Override
  public List<FieldOfStudy> getAllFieldsOfStudy() throws SQLException {
    logger.debug(() -> Util.format(allFieldsOfStudy));
    ResultSet result = allFieldsOfStudy.executeQuery();
    List<FieldOfStudy> fields = new ArrayList<>();
    while (result.next()) {
      fields.add(new FieldOfStudy(result.getInt(1), result.getString(2), result.getString(3)));
    }
    return fields;
  }

  @Override
  public Optional<FieldOfStudy> getFieldByName(String name) throws SQLException {
    getFieldByName.setString(1, name);
    logger.debug(() -> Util.format(getFieldByName));
    ResultSet set = getFieldByName.executeQuery();
    if (set.next()) {
      return Optional.of(new FieldOfStudy(set.getInt(1), set.getString(2), set.getString(3)));
    } else {
      return Optional.empty();
    }
  }

  @Override
  public Optional<FieldOfStudy> getFieldById(int id) throws SQLException {
    getFieldById.setInt(1, id);
    logger.debug(() -> Util.format(getFieldById));
    ResultSet set = getFieldById.executeQuery();
    if (set.next()) {
      return Optional.of(new FieldOfStudy(set.getInt(1), set.getString(2), set.getString(3)));
    } else {
      return Optional.empty();
    }
  }

  @Override
  public void updateFieldOfStudy(String newValue, int id) throws SQLException {
    try {
      updateFieldOfStudy.setString(1, newValue);
      updateFieldOfStudy.setInt(2, id);
      logger.debug(() -> Util.format(updateFieldOfStudy));
      updateFieldOfStudy.executeUpdate();
      connection.commit();
    } catch (SQLException e) {
      logger.catching(Level.ERROR, e);
      logger.error("Failed to update field of study name to {} where id = {}", newValue, id);
      rollBack(connection);
      throw e;
    }
  }
}
