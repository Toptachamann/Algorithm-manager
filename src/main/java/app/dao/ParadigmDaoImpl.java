package app.dao;

import app.auxiliary.Connector;
import app.auxiliary.Util;
import app.model.DesignParadigm;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ParadigmDaoImpl implements ParadigmDao {
  private static final Logger logger = LogManager.getLogger(ParadigmDaoImpl.class);

  private Connection connection;

  private PreparedStatement insertParadigmByName;
  private PreparedStatement insertParadigm;
  private PreparedStatement allParadigms;
  private PreparedStatement getParadigmById;
  private PreparedStatement getParadigmByName;
  private PreparedStatement updateParadigm;

  public ParadigmDaoImpl() throws SQLException {
    connection = Connector.getConnection();
    insertParadigm =
        connection.prepareStatement(
            "INSERT INTO design_paradigm (paradigm, description) VALUE (?, ?)");
    insertParadigmByName =
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
  public DesignParadigm insertParadigm(String paradigm) throws SQLException {
    insertParadigmByName.setString(1, paradigm);
    logger.debug(() -> Util.format(insertParadigmByName));
    insertParadigmByName.executeUpdate();
    return new DesignParadigm(Util.getLastId(connection), paradigm);
  }

  @Override
  public DesignParadigm insertParadigm(String paradigm, String description) throws SQLException {
    insertParadigm.setString(1, paradigm);
    insertParadigm.setString(2, description);
    logger.debug(() -> Util.format(insertParadigm));
    insertParadigm.executeUpdate();
    return new DesignParadigm(Util.getLastId(connection), paradigm, description);
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
    updateParadigm.setString(1, newName);
    updateParadigm.setInt(2, id);
    logger.debug(() -> Util.format(updateParadigm));
    updateParadigm.executeUpdate();
  }
}
