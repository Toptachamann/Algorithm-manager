package app.dao;

import app.auxiliary.Connector;
import app.auxiliary.Util;
import app.model.AreaOfUse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.geom.Area;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AreaDaoImpl implements AreaDao {
  private static final Logger logger = LogManager.getLogger(AreaDaoImpl.class);

  private Connection connection;

  private PreparedStatement createAreaByName;
  private PreparedStatement createArea;
  private PreparedStatement createApp;
  private PreparedStatement getAllAreas;
  private PreparedStatement getAreaById;
  private PreparedStatement getAreaByName;
  private PreparedStatement getAreasByAlgo;
  private PreparedStatement containsApp;
  private PreparedStatement deleteAreaById;

  public AreaDaoImpl() throws SQLException {
    connection = Connector.getConnection();
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
    getAreasByAlgo =
        connection.prepareStatement(
            "SELECT area_id, area, description "
                + "FROM (SELECT app_area_id FROM algorithm_application WHERE app_algorithm_id = ?) AS areas " +
                "INNER JOIN area_of_use ON areas.app_area_id = area_id");
    deleteAreaById =
        connection.prepareStatement("DELETE FROM algorithms.area_of_use WHERE area_id = ?");
    createApp =
        connection.prepareStatement(
            "INSERT INTO algorithm_application (app_algorithm_id, app_area_id) VALUE (?, ?)");
    containsApp =
        connection.prepareStatement(
            "SELECT COUNT(*) FROM algorithm_application WHERE app_algorithm_id = ? AND app_area_id = ?");
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
  public AreaOfUse createAreaOfUse(String area, String description) throws SQLException {
    createArea.setString(1, area);
    createArea.setString(2, description);
    logger.debug(() -> Util.format(createArea));
    createArea.executeUpdate();
    return new AreaOfUse(Util.getLastId(connection), area, description);
  }

  @Override
  public AreaOfUse createAreaOfUse(String area) throws SQLException {
    createAreaByName.setString(1, area);
    logger.debug(() -> Util.format(createAreaByName));
    createAreaByName.executeUpdate();
    return new AreaOfUse(Util.getLastId(connection), area);
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
    deleteAreaById.setInt(1, id);
    logger.debug(() -> Util.format(deleteAreaById));
    deleteAreaById.executeUpdate();
  }

  @Override
  public void createApplication(int algorithmId, int areaId) throws SQLException {
    createApp.setInt(1, algorithmId);
    createApp.setInt(2, areaId);
    logger.debug(() -> Util.format(createApp));
    createApp.executeUpdate();
  }

  @Override
  public boolean containsApplication(int algorithmId, int areaId) throws SQLException {
    containsApp.setInt(1, algorithmId);
    containsApp.setInt(2, areaId);
    logger.debug(() -> Util.format(containsApp));
    ResultSet set = containsApp.executeQuery();
    set.next();
    return set.getInt(1) == 1;
  }

  @Override
  public List<AreaOfUse> getAreasOfUse(int algorithmId) throws SQLException {
    getAreasByAlgo.setInt(1, algorithmId);
    logger.debug(()->Util.format(getAreasByAlgo));
    List<AreaOfUse> areas = new ArrayList<>();
    ResultSet set = getAreasByAlgo.executeQuery();
    while(set.next()){
      areas.add(new AreaOfUse(set.getInt(1), set.getString(2), set.getString(3)));
    }
    return areas;
  }
}
