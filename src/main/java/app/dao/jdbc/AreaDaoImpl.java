package app.dao.jdbc;

import app.auxiliary.Util;
import app.dao.interf.AreaDao;
import app.model.AreaOfUse;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AreaDaoImpl extends AbstractDao implements AreaDao {
  private static final Logger logger = LogManager.getLogger(AreaDaoImpl.class);

  private PreparedStatement createAreaByName;
  private PreparedStatement createArea;
  private PreparedStatement getAllAreas;
  private PreparedStatement getAreaById;
  private PreparedStatement getAreaByName;
  private PreparedStatement deleteAreaById;

  public AreaDaoImpl() throws SQLException {
    createArea =
        connection.prepareStatement("INSERT INTO area_of_use (area, description) VALUE (?, ?)");
    createAreaByName = connection.prepareStatement("INSERT INTO area_of_use (area) VALUE (?)");
    getAllAreas = connection.prepareStatement("SELECT area_id, area, description FROM area_of_use");
    getAreaById =
        connection.prepareStatement(
            "SELECT area_id, area, description FROM area_of_use WHERE area_id = ?");
    getAreaByName =
        connection.prepareStatement(
            "SELECT area_id, area, description FROM area_of_use WHERE area = ?");
    deleteAreaById =
        connection.prepareStatement("DELETE FROM algorithms.area_of_use WHERE area_id = ?");
  }

  @Override
  public AreaOfUse createAreaOfUse(String area, String description) throws SQLException {
    try {
      createArea.setString(1, area);
      createArea.setString(2, description);
      logger.debug(() -> Util.format(createArea));
      createArea.executeUpdate();
      int id = getLastId(connection);
      connection.commit();
      return new AreaOfUse(id, area, description);
    } catch (SQLException e) {
      logger.catching(Level.ERROR, e);
      logger.error("Failed to create area of use {} with description {}", area, description);
      rollBack(connection);
      throw e;
    }
  }

  @Override
  public AreaOfUse createAreaOfUse(String area) throws SQLException {
    try {
      createAreaByName.setString(1, area);
      logger.debug(() -> Util.format(createAreaByName));
      createAreaByName.executeUpdate();
      int id = getLastId(connection);
      connection.commit();
      return new AreaOfUse(id, area);
    } catch (SQLException e) {
      logger.catching(Level.ERROR, e);
      logger.error("Failed to create area {}", area);
      rollBack(connection);
      throw e;
    }
  }

  @Override
  public List<AreaOfUse> getAllAreas() throws SQLException {
    logger.debug(() -> Util.format(getAllAreas));
    ResultSet set = getAllAreas.executeQuery();
    List<AreaOfUse> result = new ArrayList<>();
    while (set.next()) {
      result.add(new AreaOfUse(set.getInt(1), set.getString(2), set.getString(3)));
    }
    return result;
  }

  @Override
  public Optional<AreaOfUse> getAreaById(int id) throws SQLException {
    getAreaById.setInt(1, id);
    logger.debug(() -> Util.format(getAreaById));
    ResultSet set = getAreaById.executeQuery();
    if (set.next()) {
      return Optional.of(new AreaOfUse(set.getInt(1), set.getString(2), set.getString(3)));
    } else {
      return Optional.empty();
    }
  }

  @Override
  public Optional<AreaOfUse> getAreaByName(String name) throws SQLException {
    getAreaByName.setString(1, name);
    logger.debug(() -> Util.format(getAreaByName));
    ResultSet set = getAreaByName.executeQuery();
    if (set.next()) {
      return Optional.of(new AreaOfUse(set.getInt(1), set.getString(2), set.getString(3)));
    } else {
      return Optional.empty();
    }
  }

  @Override
  public void deleteAreaOfUse(int id) throws SQLException {
    try {
      deleteAreaById.setInt(1, id);
      logger.debug(() -> Util.format(deleteAreaById));
      deleteAreaById.executeUpdate();
      connection.commit();
    } catch (SQLException e) {
      logger.catching(Level.ERROR, e);
      logger.error("Failed to delete area of use with id {}", id);
      rollBack(connection);
      throw e;
    }
  }
}
