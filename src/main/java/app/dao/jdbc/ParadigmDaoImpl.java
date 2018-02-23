package app.dao.jdbc;

import app.auxiliary.Util;
import app.dao.interf.ParadigmDao;
import app.model.DesignParadigm;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ParadigmDaoImpl extends AbstractDao implements ParadigmDao {
  private static final Logger logger = LogManager.getLogger(ParadigmDaoImpl.class);


  private PreparedStatement createParadigmByName;
  private PreparedStatement createParadigm;
  private PreparedStatement allParadigms;
  private PreparedStatement getParadigmById;
  private PreparedStatement getParadigmByName;
  private PreparedStatement updateParadigm;

  public ParadigmDaoImpl() throws SQLException {
    createParadigm =
        connection.prepareStatement(
            "INSERT INTO design_paradigm (paradigm, description) VALUE (?, ?)");
    createParadigmByName =
        connection.prepareStatement("INSERT INTO design_paradigm (paradigm) VALUE (?)");
    allParadigms = connection.prepareStatement("SELECT * FROM design_paradigm");
    getParadigmById =
        connection.prepareStatement("SELECT * FROM design_paradigm WHERE paradigm_id = ?");
    getParadigmByName =
        connection.prepareStatement("SELECT * FROM design_paradigm WHERE paradigm = ?");
    updateParadigm =
        connection.prepareStatement(
            "UPDATE design_paradigm SET paradigm = ? WHERE paradigm_id = ?");
  }

  @Override
  public DesignParadigm createParadigm(String paradigm) throws SQLException {
    try {
      createParadigmByName.setString(1, paradigm);
      logger.debug(() -> Util.format(createParadigmByName));
      createParadigmByName.executeUpdate();
      int id = getLastId(connection);
      connection.commit();
      return new DesignParadigm(id, paradigm);
    } catch (SQLException e) {
      logger.catching(Level.ERROR, e);
      logger.error("Failed to create design paradigm {}", paradigm);
      rollBack(connection);
      throw e;
    }
  }

  @Override
  public DesignParadigm createParadigm(String paradigm, String description) throws SQLException {
    try {
      createParadigm.setString(1, paradigm);
      createParadigm.setString(2, description);
      logger.debug(() -> Util.format(createParadigm));
      createParadigm.executeUpdate();
      int id = getLastId(connection);
      connection.commit();
      return new DesignParadigm(id, paradigm, description);
    } catch (SQLException e) {
      logger.catching(Level.ERROR, e);
      logger.error(
          "Failed to create design paradigm {} with description {}", paradigm, description);
      rollBack(connection);
      throw e;
    }
  }

  @Override
  public List<DesignParadigm> getAllDesignParadigms() throws SQLException {
    logger.debug(() -> Util.format(allParadigms));
    ResultSet result = allParadigms.executeQuery();
    List<DesignParadigm> paradigms = new ArrayList<>();
    while (result.next()) {
      paradigms.add(new DesignParadigm(result.getInt(1), result.getString(2), result.getString(3)));
    }
    return paradigms;
  }

  @Override
  public Optional<DesignParadigm> getParadigmByName(String name) throws SQLException {
    getParadigmByName.setString(1, name);
    logger.debug(() -> Util.format(getParadigmByName));
    ResultSet set = getParadigmByName.executeQuery();
    if (set.next()) {
      return Optional.of(new DesignParadigm(set.getInt(1), set.getString(2), set.getString(3)));
    } else {
      return Optional.empty();
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
  public void updateDesignParadigm(String newName, int id) throws SQLException {
    try {
      updateParadigm.setString(1, newName);
      updateParadigm.setInt(2, id);
      logger.debug(() -> Util.format(updateParadigm));
      updateParadigm.executeUpdate();
      connection.commit();
    } catch (SQLException e) {
      logger.catching(Level.ERROR, e);
      logger.error("Failed to set paradigm name to {} where paradigm id = {}", newName, id);
      rollBack(connection);
      throw e;
    }
  }
}
