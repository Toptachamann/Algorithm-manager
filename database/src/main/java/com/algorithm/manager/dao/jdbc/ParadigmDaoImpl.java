package com.algorithm.manager.dao.jdbc;

import com.algorithm.manager.auxiliary.Util;
import com.algorithm.manager.dao.interf.ParadigmDao;
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

public class ParadigmDaoImpl extends AbstractDao implements ParadigmDao {
  private static final Logger logger = LogManager.getLogger(ParadigmDaoImpl.class);

  private PreparedStatement createParadigm;
  private PreparedStatement allParadigms;
  private PreparedStatement getParadigmById;
  private PreparedStatement getParadigmByName;
  private PreparedStatement updateParadigm;
  private PreparedStatement deleteParadigm;

  public ParadigmDaoImpl() throws SQLException {
    createParadigm =
        connection.prepareStatement(
            "INSERT INTO design_paradigm (paradigm, description) VALUE (?, ?)");
    allParadigms = connection.prepareStatement("SELECT * FROM design_paradigm");
    getParadigmById =
        connection.prepareStatement("SELECT * FROM design_paradigm WHERE paradigm_id = ?");
    getParadigmByName =
        connection.prepareStatement("SELECT * FROM design_paradigm WHERE paradigm = ?");
    updateParadigm =
        connection.prepareStatement(
            "UPDATE design_paradigm SET paradigm = ? WHERE paradigm_id = ?");
    deleteParadigm =
        connection.prepareStatement("DELETE FROM design_paradigm WHERE paradigm_id = ?");
  }

  @Override
  public void persist(DesignParadigm paradigm) throws SQLException {
    try {
      createParadigm.setString(1, paradigm.getParadigm());
      if (paradigm.getDescription() == null) {
        createParadigm.setNull(2, Types.VARCHAR);
      } else {
        createParadigm.setString(2, paradigm.getDescription());
      }
      logger.debug(() -> Util.format(createParadigm));
      createParadigm.executeUpdate();
      paradigm.setId(getLastId(connection));
      connection.commit();
    } catch (SQLException e) {
      logger.catching(Level.ERROR, e);
      logger.error("Failed to persist design paradigm {}", paradigm);
      rollBack(connection);
      throw e;
    }
  }

  @Override
  public List<DesignParadigm> getAllParadigms() throws SQLException {
    logger.debug(() -> Util.format(allParadigms));
    ResultSet result = allParadigms.executeQuery();
    List<DesignParadigm> paradigms = new ArrayList<>();
    while (result.next()) {
      paradigms.add(new DesignParadigm(result.getInt(1), result.getString(2), result.getString(3)));
    }
    return paradigms;
  }

  @Override
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

  @Override
  public void delete(DesignParadigm paradigm) throws Exception {
    try {
      deleteParadigm.setInt(1, paradigm.getId());
      logger.debug(() -> Util.format(deleteParadigm));
      deleteParadigm.executeUpdate();
      connection.commit();
    } catch (SQLException e) {
      logger.catching(Level.ERROR, e);
      logger.error("Failed to delete design paradigm {}", paradigm);
      rollBack(connection);
      throw e;
    }
  }

  @Override
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

  @Override
  public void setParadigm(DesignParadigm paradigm, String newName) throws SQLException {
    try {
      updateParadigm.setString(1, newName);
      updateParadigm.setInt(2, paradigm.getId());
      logger.debug(() -> Util.format(updateParadigm));
      updateParadigm.executeUpdate();
      connection.commit();
    } catch (SQLException e) {
      logger.catching(Level.ERROR, e);
      logger.error("Failed to set paradigm of the {} to {}", paradigm, newName);
      rollBack(connection);
      throw e;
    }
  }
}
