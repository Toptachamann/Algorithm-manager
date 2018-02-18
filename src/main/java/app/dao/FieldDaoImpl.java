package app.dao;

import app.auxiliary.Connector;
import app.auxiliary.Util;
import app.model.FieldOfStudy;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FieldDaoImpl implements FieldDao {
  private static final Logger logger = LogManager.getLogger(FieldDaoImpl.class);

  private Connection connection;

  private PreparedStatement insertFieldByName;
  private PreparedStatement insertField;
  private PreparedStatement allFieldsOfStudy;
  private PreparedStatement getFieldById;
  private PreparedStatement getFieldByName;
  private PreparedStatement updateFieldOfStudy;

  public FieldDaoImpl() throws SQLException {
    connection = Connector.getConnection();
    allFieldsOfStudy = connection.prepareStatement("SELECT * FROM field_of_study");
    insertFieldByName = connection.prepareStatement("INSERT INTO field_of_study (field) VALUE (?)");
    insertField =
        connection.prepareStatement("INSERT INTO field_of_study (field, description) VALUE (?, ?)");
    getFieldById = connection.prepareStatement("SELECT * FROM field_of_study WHERE field_id = ?");
    getFieldByName = connection.prepareStatement("SELECT * FROM field_of_study WHERE field = ?");
    updateFieldOfStudy =
        connection.prepareStatement(
            "UPDATE algorithms.field_of_study "
                + "SET algorithms.field_of_study.field = ? WHERE algorithms.field_of_study.field_id = ?");
  }

  @Override
  public FieldOfStudy insertFieldOfStudy(String fieldOfStudy) throws SQLException {
    insertFieldByName.setString(1, fieldOfStudy);
    logger.debug(() -> Util.format(insertFieldByName));
    insertFieldByName.executeUpdate();
    return new FieldOfStudy(Util.getLastId(connection), fieldOfStudy);
  }

  @Override
  public FieldOfStudy insertFieldOfStudy(String fieldOfStudy, String description)
      throws SQLException {
    insertField.setString(1, fieldOfStudy);
    insertField.setString(2, description);
    logger.debug(() -> Util.format(insertField));
    insertField.executeUpdate();
    return new FieldOfStudy(Util.getLastId(connection), fieldOfStudy, description);
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
    updateFieldOfStudy.setString(1, newValue);
    updateFieldOfStudy.setInt(2, id);
    logger.debug(() -> Util.format(updateFieldOfStudy));
    updateFieldOfStudy.executeUpdate();
  }
}
