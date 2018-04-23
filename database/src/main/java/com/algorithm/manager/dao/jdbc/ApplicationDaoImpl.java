package com.algorithm.manager.dao.jdbc;

import com.algorithm.manager.auxiliary.Util;
import com.algorithm.manager.model.Algorithm;
import com.algorithm.manager.model.Application;
import com.algorithm.manager.model.AreaOfUse;
import com.algorithm.manager.model.DesignParadigm;
import com.algorithm.manager.model.FieldOfStudy;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ApplicationDaoImpl extends AbstractDao {
  private static final Logger logger = LogManager.getLogger(ApplicationDaoImpl.class);

  private PreparedStatement createApp;
  private PreparedStatement deleteApplication;
  private PreparedStatement getApplicationsByArea;
  private PreparedStatement getApplicationsByAlgorithm;
  private PreparedStatement getApplication;

  public ApplicationDaoImpl() throws SQLException {
    createApp =
        connection.prepareStatement(
            "INSERT INTO algorithm_application (app_algorithm_id, app_area_id) VALUE (?, ?)");
    getApplication =
        connection.prepareStatement(
            "SELECT application_id FROM algorithm_application WHERE app_algorithm_id = ? AND app_area_id = ?");
    deleteApplication =
        connection.prepareStatement(
            "DELETE FROM algorithm_application WHERE app_algorithm_id = ? AND app_area_id = ?");
    getApplicationsByArea =
        connection.prepareStatement(
            "SELECT \n"
                + "    algos.application_id,\n"
                + "    alg.algorithm_id,\n"
                + "    alg.algorithm,\n"
                + "    alg.complexity,\n"
                + "    dp.paradigm_id,\n"
                + "    dp.paradigm,\n"
                + "    dp.description,\n"
                + "    f.field_id,\n"
                + "    f.field,\n"
                + "    f.description\n"
                + "FROM\n"
                + "    (SELECT \n"
                + "        application_id\n"
                + "        app_algorithm_id\n"
                + "    FROM\n"
                + "        algorithm_application\n"
                + "    WHERE\n"
                + "        app_area_id = ?) AS algos\n"
                + "        INNER JOIN\n"
                + "    algorithm AS alg ON algos.app_algorithm_id = algorithm_id\n"
                + "        INNER JOIN\n"
                + "    design_paradigm AS dp ON algo_paradigm_id = paradigm_id\n"
                + "        INNER JOIN\n"
                + "    field_of_study AS f ON algo_field_id = field_id\n"
                + "ORDER BY algorithm_id");
    getApplicationsByAlgorithm =
        connection.prepareStatement(
            "SELECT areas.application_id, area_id, area, description "
                + "FROM (SELECT application_id, app_area_id FROM algorithm_application WHERE app_algorithm_id = ?) AS areas "
                + "INNER JOIN area_of_use ON areas.app_area_id = area_id");
  }

  
  public void persist(Application application) throws SQLException {
    try {
      createApp.setInt(1, application.getAlgorithm().getId());
      createApp.setInt(2, application.getAreaOfUse().getId());
      logger.debug(() -> Util.format(createApp));
      createApp.executeUpdate();
      application.setId(getLastId(connection));
      connection.commit();
    } catch (SQLException e) {
      logger.catching(Level.ERROR, e);
      logger.error("Failed to persistAlgorithm application {}", application);
      rollBack(connection);
      throw e;
    }
  }

  
  public Optional<Application> getApplicationOf(Algorithm algorithm, AreaOfUse areaOfUse)
      throws SQLException {
    try {
      getApplication.setInt(1, algorithm.getId());
      getApplication.setInt(2, areaOfUse.getId());
      logger.debug(() -> Util.format(getApplication));
      ResultSet set = getApplication.executeQuery();
      return set.next()
          ? Optional.of(new Application(set.getInt(1), algorithm, areaOfUse))
          : Optional.empty();
    } catch (SQLException e) {
      logger.catching(Level.ERROR, e);
      logger.error(
          "Failed to get application of algorithm {} and area of use {}", algorithm, areaOfUse);
      throw e;
    }
  }

  
  public void delete(Application application) throws SQLException {
    try {
      deleteApplication.setInt(1, application.getAlgorithm().getId());
      deleteApplication.setInt(2, application.getAreaOfUse().getId());
      logger.debug(() -> Util.format(deleteApplication));
      deleteApplication.executeUpdate();
      connection.commit();
    } catch (SQLException e) {
      logger.catching(Level.ERROR, e);
      logger.error(
          "Failed to deleteById algorithm application {}", application);
      rollBack(connection);
      throw e;
    }
  }

  
  public List<Application> getApplicationsByArea(AreaOfUse area) throws SQLException {
    try {
      getApplicationsByArea.setInt(1, area.getId());
      logger.debug(() -> Util.format(getApplicationsByArea));
      ResultSet set = getApplicationsByArea.executeQuery();
      List<Application> applications = new ArrayList<>();
      while (set.next()) {
        DesignParadigm paradigm =
            new DesignParadigm(set.getInt(5), set.getString(6), set.getString(7));
        FieldOfStudy fieldOfStudy =
            new FieldOfStudy(set.getInt(8), set.getString(9), set.getString(10));
        Algorithm algorithm =
            new Algorithm(
                set.getInt(2), set.getString(3), set.getString(4), paradigm, fieldOfStudy);
        applications.add(new Application(set.getInt(1), algorithm, area));
      }
      return applications;
    } catch (SQLException e) {
      logger.catching(Level.ERROR, e);
      logger.error("Failed to obtain algorithms by area {}", area);
      throw e;
    }
  }

  
  public List<Application> getApplicationsByAlgorithm(Algorithm algorithm) throws Exception {
    try {
      getApplicationsByAlgorithm.setInt(1, algorithm.getId());
      logger.debug(() -> Util.format(getApplicationsByAlgorithm));
      ResultSet set = getApplicationsByAlgorithm.executeQuery();
      List<Application> applications = new ArrayList<>();
      while (set.next()) {
        AreaOfUse areaOfUse = new AreaOfUse(set.getInt(2), set.getString(3), set.getString(4));
        applications.add(new Application(set.getInt(1), algorithm, areaOfUse));
      }
      return applications;
    } catch (SQLException e) {
      logger.catching(Level.ERROR, e);
      logger.error("Failed to get applications by algorithm {}", algorithm);
      throw e;
    }
  }
}
