package app.dao;

import app.auxiliary.Connector;
import app.auxiliary.Util;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AlgorithmApplicationDaoImpl extends AbstractDao implements AlgorithmApplicationDao {
  private static final Logger logger = LogManager.getLogger(AlgorithmApplicationDaoImpl.class);

  private Connection connection;

  private PreparedStatement createApp;
  private PreparedStatement containsApp;
  private PreparedStatement deleteApplication;

  public AlgorithmApplicationDaoImpl() throws SQLException {
    connection = Connector.getConnection();
    createApp =
        connection.prepareStatement(
            "INSERT INTO algorithm_application (app_algorithm_id, app_area_id) VALUE (?, ?)");
    containsApp =
        connection.prepareStatement(
            "SELECT COUNT(*) FROM algorithm_application WHERE app_algorithm_id = ? AND app_area_id = ?");
    deleteApplication =
        connection.prepareStatement(
            "DELETE FROM algorithm_application WHERE app_algorithm_id = ? AND app_area_id = ?");
  }

  @Override
  public void createApplication(int algorithmId, int areaId) throws SQLException {
    try {
      createApp.setInt(1, algorithmId);
      createApp.setInt(2, areaId);
      logger.debug(() -> Util.format(createApp));
      createApp.executeUpdate();
      connection.commit();
    } catch (SQLException e) {
      logger.catching(Level.ERROR, e);
      logger.error(
          "Failed to create application. Algorithm id = {}, area id = {}", algorithmId, areaId);
      rollBack(connection);
      throw e;
    }
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
  public void deleteApplication(int algorithmId, int areaId) throws SQLException {
    try {
      deleteApplication.setInt(1, algorithmId);
      deleteApplication.setInt(2, areaId);
      logger.debug(() -> Util.format(deleteApplication));
      deleteApplication.executeUpdate();
      connection.commit();
    } catch (SQLException e) {
      logger.catching(Level.ERROR, e);
      logger.error(
          "Failed to delete algorithm application, algorithm id = {}, areaId = {}",
          algorithmId,
          areaId);
      rollBack(connection);
      throw e;
    }
  }
}
