package com.algorithm.manager.dao.jdbc;

import com.algorithm.manager.auxiliary.Util;
import com.algorithm.manager.model.DesignParadigm;
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

public class ParadigmDaoImpl extends AbstractDao {
  private static final Logger logger = LogManager.getLogger(ParadigmDaoImpl.class);

  private PreparedStatement createParadigm;
  private PreparedStatement allParadigms;
  private PreparedStatement getParadigmById;
  private PreparedStatement getParadigmByName;
  private PreparedStatement merge;
  private PreparedStatement deleteById;
  private PreparedStatement deleteByParadigm;

  public ParadigmDaoImpl() throws SQLException {
    createParadigm =
        connection.prepareStatement(
            "INSERT INTO design_paradigm (paradigm, description) VALUE (?, ?)");
    allParadigms = connection.prepareStatement("SELECT * FROM design_paradigm");
    getParadigmById =
        connection.prepareStatement("SELECT * FROM design_paradigm WHERE paradigm_id = ?");
    getParadigmByName =
        connection.prepareStatement("SELECT * FROM design_paradigm WHERE paradigm = ?");
    merge =
        connection.prepareStatement(
            "UPDATE design_paradigm SET paradigm = ?, description = ? WHERE paradigm_id = ?");
    deleteById = connection.prepareStatement("DELETE FROM design_paradigm WHERE paradigm_id = ?");
    deleteByParadigm =
        connection.prepareStatement(
            "DELETE FROM algorithms.design_paradigm WHERE algorithms.design_paradigm.paradigm = ?");
  }

  public void persist(DesignParadigm paradigm) throws SQLException {
    try {
      createParadigm.setString(1, paradigm.getName());
      if (paradigm.getDescription() == null) {
        createParadigm.setNull(2, Types.VARCHAR);
      } else {
        createParadigm.setString(2, paradigm.getDescription());
      }
      logger.debug(() -> Util.format(createParadigm));
      createParadigm.executeUpdate();
      paradigm.setDesignParadigmId(getLastId(connection));
      connection.commit();
    } catch (SQLException e) {
      logger.catching(Level.ERROR, e);
      logger.error("Failed to persistAlgorithm design paradigm {}", paradigm);
      rollBack(connection);
      throw e;
    }
  }

  public List<DesignParadigm> getAllParadigms() throws SQLException {
    logger.debug(() -> Util.format(allParadigms));
    ResultSet result = allParadigms.executeQuery();
    List<DesignParadigm> paradigms = new ArrayList<>();
    while (result.next()) {
      paradigms.add(new DesignParadigm(result.getInt(1), result.getString(2), result.getString(3)));
    }
    return paradigms;
  }

  public Optional<DesignParadigm> getParadigmByParadigm(String paradigm) throws SQLException {
    getParadigmByName.setString(1, paradigm);
    logger.debug(() -> Util.format(getParadigmByName));
    ResultSet set = getParadigmByName.executeQuery();
    if (set.next()) {
      return Optional.of(new DesignParadigm(set.getInt(1), set.getString(2), set.getString(3)));
    } else {
      return Optional.empty();
    }
  }

  public void delete(DesignParadigm paradigm) throws Exception {
    try {
      deleteById.setInt(1, paradigm.getDesignParadigmId());
      logger.debug(() -> Util.format(deleteById));
      deleteById.executeUpdate();
      connection.commit();
    } catch (SQLException e) {
      logger.catching(Level.ERROR, e);
      logger.error("Failed to deleteById design paradigm {}", paradigm);
      rollBack(connection);
      throw e;
    }
  }

  public Optional<DesignParadigm> getParadigmById(int id) throws SQLException {
    getParadigmById.setInt(1, id);
    logger.debug(() -> Util.format(getParadigmById));
    ResultSet set = getParadigmById.executeQuery();
    if (set.next()) {
      return Optional.of(new DesignParadigm(set.getInt(1), set.getString(2), set.getString(3)));
    } else {
      return Optional.empty();
    }
  }

  private boolean containsParadigm(DesignParadigm paradigm) throws Exception {
    return getParadigmById(paradigm.getDesignParadigmId()).isPresent();
  }

  public void merge(DesignParadigm paradigm) throws Exception {
    if (!containsParadigm(paradigm)) {
      persist(paradigm);
    } else {
      try {
        merge.setString(1, paradigm.getName());
        merge.setString(2, paradigm.getDescription());
        merge.setInt(3, paradigm.getDesignParadigmId());
        logger.debug(() -> Util.format(merge));
        merge.executeUpdate();
        connection.commit();
      } catch (SQLException e) {
        logger.catching(Level.ERROR, e);
        logger.error("Failed to merge paradigm {}", paradigm);
        rollBack(connection);
        throw e;
      }
    }
  }

  public void deleteByParadigm(DesignParadigm paradigm) throws Exception {
    try {
      deleteByParadigm.setString(1, paradigm.getName());
      logger.debug(() -> Util.format(deleteByParadigm));
      deleteByParadigm.executeUpdate();
      connection.commit();
    } catch (SQLException e) {
      logger.catching(Level.ERROR, e);
      logger.error("Failed to delete design paradigm {} by paradigm", paradigm);
      rollBack(connection);
      throw e;
    }
  }
}
