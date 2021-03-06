package com.algorithm.manager.dao.jdbc;

import com.algorithm.manager.auxiliary.Util;
import com.algorithm.manager.model.AreaOfUse;
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

public class AreaDaoImpl extends AbstractDao {
  private static final Logger logger = LogManager.getLogger(AreaDaoImpl.class);

  private PreparedStatement createArea;
  private PreparedStatement getAllAreas;
  private PreparedStatement getAreaById;
  private PreparedStatement getAreaByName;
  private PreparedStatement deleteAreaById;
  private PreparedStatement deleteByArea;

  public AreaDaoImpl() throws SQLException {
    createArea =
        connection.prepareStatement("INSERT INTO area_of_use (area, description) VALUE (?, ?)");
    getAllAreas = connection.prepareStatement("SELECT area_id, area, description FROM area_of_use");
    getAreaById =
        connection.prepareStatement(
            "SELECT area_id, area, description FROM area_of_use WHERE area_id = ?");
    getAreaByName =
        connection.prepareStatement(
            "SELECT area_id, area, description FROM area_of_use WHERE area = ?");
    deleteAreaById =
        connection.prepareStatement("DELETE FROM algorithms.area_of_use WHERE area_id = ?");
    deleteByArea =
        connection.prepareStatement(
            "DELETE FROM algorithms.area_of_use WHERE algorithms.area_of_use.area = ?");
  }

  
  public void persist(AreaOfUse areaOfUse) throws SQLException {
    try {
      createArea.setString(1, areaOfUse.getName());
      if (areaOfUse.getDescription() == null) {
        createArea.setNull(2, Types.VARCHAR);
      } else {
        createArea.setString(2, areaOfUse.getDescription());
      }
      logger.debug(() -> Util.format(createArea));
      createArea.executeUpdate();
      areaOfUse.setId(getLastId(connection));
      connection.commit();
    } catch (SQLException e) {
      logger.catching(Level.ERROR, e);
      logger.error("Failed to persistAlgorithm area of use {}", areaOfUse);
      rollBack(connection);
      throw e;
    }
  }

  
  public List<AreaOfUse> getAllAreas() throws SQLException {
    logger.debug(() -> Util.format(getAllAreas));
    ResultSet set = getAllAreas.executeQuery();
    List<AreaOfUse> result = new ArrayList<>();
    while (set.next()) {
      result.add(new AreaOfUse(set.getInt(1), set.getString(2), set.getString(3)));
    }
    return result;
  }

  
  public Optional<AreaOfUse> getAreaOfUseById(int id) throws SQLException {
    getAreaById.setInt(1, id);
    logger.debug(() -> Util.format(getAreaById));
    ResultSet set = getAreaById.executeQuery();
    if (set.next()) {
      return Optional.of(new AreaOfUse(set.getInt(1), set.getString(2), set.getString(3)));
    } else {
      return Optional.empty();
    }
  }

  
  public Optional<AreaOfUse> getAreaOfUseByName(String name) throws SQLException {
    getAreaByName.setString(1, name);
    logger.debug(() -> Util.format(getAreaByName));
    ResultSet set = getAreaByName.executeQuery();
    if (set.next()) {
      return Optional.of(new AreaOfUse(set.getInt(1), set.getString(2), set.getString(3)));
    } else {
      return Optional.empty();
    }
  }

  
  public void deleteById(AreaOfUse areaOfUse) throws SQLException {
    try {
      deleteAreaById.setInt(1, areaOfUse.getId());
      logger.debug(() -> Util.format(deleteAreaById));
      deleteAreaById.executeUpdate();
      connection.commit();
    } catch (SQLException e) {
      logger.catching(Level.ERROR, e);
      logger.error("Failed to deleteById area of use", areaOfUse);
      rollBack(connection);
      throw e;
    }
  }

  
  public void delete(AreaOfUse areaOfUse) throws Exception {
    try {
      deleteByArea.setString(1, areaOfUse.getName());
      logger.debug(() -> Util.format(deleteByArea));
      deleteByArea.executeUpdate();
      connection.commit();
    } catch (Exception e) {
      logger.catching(Level.ERROR, e);
      logger.error("Failed to delete area of use {} by area", areaOfUse);
      rollBack(connection);
      throw e;
    }
  }
}
