package com.algorithm.manager.dao.jdbc;

import com.algorithm.manager.auxiliary.Util;
import com.algorithm.manager.model.FieldOfStudy;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FieldDaoImpl extends AbstractDao {
  private static final Logger logger = LogManager.getLogger(FieldDaoImpl.class);

  private PreparedStatement createField;
  private PreparedStatement allFieldsOfStudy;
  private PreparedStatement getFieldById;
  private PreparedStatement getFieldByName;
  private PreparedStatement merge;
  private PreparedStatement deleteFieldById;
  private PreparedStatement deleteByField;

  public FieldDaoImpl() throws SQLException {
    allFieldsOfStudy = connection.prepareStatement("SELECT * FROM field_of_study");
    createField =
        connection.prepareStatement("INSERT INTO field_of_study (field, description) VALUE (?, ?)");
    getFieldById = connection.prepareStatement("SELECT * FROM field_of_study WHERE field_id = ?");
    getFieldByName = connection.prepareStatement("SELECT * FROM field_of_study WHERE field = ?");
    merge =
        connection.prepareStatement(
            "UPDATE algorithms.field_of_study "
                + "SET algorithms.field_of_study.field = ?, field_of_study.description = ? WHERE algorithms.field_of_study.field_id = ?");
    deleteFieldById = connection.prepareStatement("DELETE FROM field_of_study WHERE field_id = ?");
    deleteByField =
        connection.prepareStatement(
            "DELETE FROM algorithms.field_of_study WHERE algorithms.field_of_study.field = ?");
  }

  public void persist(FieldOfStudy fieldOfStudy) throws SQLException {
    try {
      createField.setString(1, fieldOfStudy.getName());
      if (fieldOfStudy.getDescription() == null) {
        createField.setNull(2, Types.VARCHAR);
      } else {
        createField.setString(2, fieldOfStudy.getDescription());
      }
      logger.debug(() -> Util.format(createField));
      createField.executeUpdate();
      fieldOfStudy.setId(getLastId(connection));
      connection.commit();
    } catch (SQLException e) {
      logger.catching(Level.ERROR, e);
      logger.error("Failed to persistAlgorithm field of study {}", fieldOfStudy);
      rollBack(connection);
      throw e;
    }
  }

  public List<FieldOfStudy> getAllFieldsOfStudy() throws SQLException {
    logger.debug(() -> Util.format(allFieldsOfStudy));
    ResultSet result = allFieldsOfStudy.executeQuery();
    List<FieldOfStudy> fields = new ArrayList<>();
    while (result.next()) {
      fields.add(new FieldOfStudy(result.getInt(1), result.getString(2), result.getString(3)));
    }
    return fields;
  }

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

  private boolean containsFieldOfStudy(FieldOfStudy fieldOfStudy) throws SQLException {
    return getFieldById(fieldOfStudy.getId()).isPresent();
  }

  public void merge(FieldOfStudy fieldOfStudy) throws Exception {
    if (!containsFieldOfStudy(fieldOfStudy)) {
      persist(fieldOfStudy);
    } else {
      try {
        merge.setString(1, fieldOfStudy.getName());
        merge.setString(2, fieldOfStudy.getDescription());
        merge.setInt(3, fieldOfStudy.getId());
        logger.debug(() -> Util.format(merge));
        merge.executeUpdate();
        connection.commit();
      } catch (SQLException e) {
        logger.catching(Level.ERROR, e);
        logger.error("Failed to merge field of study", fieldOfStudy);
        rollBack(connection);
        throw e;
      }
    }
  }

  public void delete(FieldOfStudy fieldOfStudy) throws Exception {
    try {
      deleteFieldById.setInt(1, fieldOfStudy.getId());
      logger.debug(() -> Util.format(deleteFieldById));
      deleteFieldById.executeUpdate();
      connection.commit();
    } catch (SQLException e) {
      logger.catching(Level.ERROR, e);
      logger.error("Failed to deleteById field of study", fieldOfStudy);
      rollBack(connection);
      throw e;
    }
  }

  public void deleteByField(FieldOfStudy fieldOfStudy) throws Exception {
    try {
      deleteByField.setString(1, fieldOfStudy.getName());
      logger.debug(() -> Util.format(deleteByField));
      deleteByField.executeUpdate();
      connection.commit();
    } catch (Exception e) {
      logger.catching(Level.ERROR, e);
      logger.error("Failed to delete field of study {} by field", fieldOfStudy);
      rollBack(connection);
      throw e;
    }
  }
}
