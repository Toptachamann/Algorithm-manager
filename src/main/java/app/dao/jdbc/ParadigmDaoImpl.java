package app.dao.jdbc;

import app.auxiliary.Util;
import app.dao.interf.ParadigmDao;
import app.model.DesignParadigm;
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
  public void create(DesignParadigm paradigm) throws SQLException {
    try {
      createParadigm.setString(1, paradigm.getParadigm());
      if(paradigm.getDescription() == null){
        createParadigm.setNull(2, Types.VARCHAR);
      }else{
        createParadigm.setString(2, paradigm.getDescription());
      }
      logger.debug(() -> Util.format(createParadigm));
      createParadigm.executeUpdate();
      paradigm.setId(getLastId(connection));
      connection.commit();
    } catch (SQLException e) {
      logger.catching(Level.ERROR, e);
      logger.error(
          "Failed to create design paradigm {}", paradigm);
      rollBack(connection);
      throw e;
    }
  }

  @Override
  public List<DesignParadigm> getAll() throws SQLException {
    logger.debug(() -> Util.format(allParadigms));
    ResultSet result = allParadigms.executeQuery();
    List<DesignParadigm> paradigms = new ArrayList<>();
    while (result.next()) {
      paradigms.add(new DesignParadigm(result.getInt(1), result.getString(2), result.getString(3)));
    }
    return paradigms;
  }

  @Override
  public Optional<DesignParadigm> findByParadigm(String paradigm) throws SQLException {
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
  public void deleteDesignParadigmById(int id) throws Exception {
    try {
      deleteParadigm.setInt(1, id);
      logger.debug(() -> Util.format(deleteParadigm));
      deleteParadigm.executeUpdate();
      connection.commit();
    } catch (SQLException e) {
      logger.catching(Level.ERROR, e);
      logger.error("Failed to delete design paradigm with id {}", id);
      rollBack(connection);
      throw e;
    }
  }

  @Override
  public Optional<DesignParadigm> findById(int id) throws SQLException {
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
