package app.dao.jdbc;

import app.auxiliary.Util;
import app.dao.interf.ApplicationDao;
import app.model.Algorithm;
import app.model.Application;
import app.model.AreaOfUse;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class ApplicationDaoImpl extends AbstractDao implements ApplicationDao {
  private static final Logger logger = LogManager.getLogger(ApplicationDaoImpl.class);

  private PreparedStatement createApp;
  private PreparedStatement containsApp;
  private PreparedStatement deleteApplication;

  public ApplicationDaoImpl() throws SQLException {
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
  public Application createApplication(Algorithm algorithm, AreaOfUse areaOfUse)
      throws SQLException {
    try {
      createApp.setInt(1, algorithm.getId());
      createApp.setInt(2, areaOfUse.getId());
      logger.debug(() -> Util.format(createApp));
      createApp.executeUpdate();
      int id = getLastId(connection);
      connection.commit();
      return new Application(id, algorithm, areaOfUse);
    } catch (SQLException e) {
      logger.catching(Level.ERROR, e);
      logger.error("Failed to create application. Algorithm = {}, area = {}", algorithm, areaOfUse);
      rollBack(connection);
      throw e;
    }
  }

  @Override
  public boolean containsApplication(Algorithm algorithm, AreaOfUse areaOfUse) throws SQLException {
    containsApp.setInt(1, algorithm.getId());
    containsApp.setInt(2, areaOfUse.getId());
    logger.debug(() -> Util.format(containsApp));
    ResultSet set = containsApp.executeQuery();
    set.next();
    return set.getInt(1) == 1;
  }

  @Override
  public void deleteApplication(Algorithm algorithm, AreaOfUse areaOfUse) throws SQLException {
    try {
      deleteApplication.setInt(1, algorithm.getId());
      deleteApplication.setInt(2, areaOfUse.getId());
      logger.debug(() -> Util.format(deleteApplication));
      deleteApplication.executeUpdate();
      connection.commit();
    } catch (SQLException e) {
      logger.catching(Level.ERROR, e);
      logger.error(
          "Failed to delete algorithm application, algorithm id = {}, areaId = {}",
          algorithm,
          areaOfUse);
      rollBack(connection);
      throw e;
    }
  }

  @Override
  public List<Application> getApplicationsByArea(AreaOfUse area) throws Exception {
    return null;
  }
}
