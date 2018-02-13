package app.dao;

import app.auxiliary.Connector;
import app.model.DesignParadigm;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ParadigmDaoImpl implements ParadigmDao{
  private Connection connection;

  private PreparedStatement insertParadigmByName;
  private PreparedStatement insertParadigm;
  private PreparedStatement allParadigms;


  public ParadigmDaoImpl() throws SQLException {
    connection = Connector.getConnection();
    allParadigms = connection.prepareStatement("SELECT * FROM design_paradigm");
    insertParadigm =
        connection.prepareStatement(
            "INSERT INTO design_paradigm (paradigm, description) VALUE (?, ?)");
    insertParadigmByName =
        connection.prepareStatement("INSERT INTO design_paradigm (paradigm) VALUE (?)");
  }

  @Override
  public void insertParadigm(String paradigm) throws SQLException {
    insertParadigmByName.setString(1, paradigm);
    insertParadigmByName.executeUpdate();
  }

  @Override
  public void insertParadigm(String paradigm, String description) throws SQLException {
    insertParadigm.setString(1, paradigm);
    insertParadigm.setString(2, description);
    insertParadigm.executeUpdate();
  }

  @Override
  public List<DesignParadigm> getAllDesignParadigms() throws SQLException {
    ResultSet result = allParadigms.executeQuery();
    List<DesignParadigm> paradigms = new ArrayList<>();
    while (result.next()) {
      paradigms.add(new DesignParadigm(result.getInt(1), result.getString(2), result.getString(3)));
    }
    return paradigms;
  }

  @Override
  public DesignParadigm getParadigmByName(String name) throws SQLException {
    Statement query = connection.createStatement();
    ResultSet set =
        query.executeQuery("SELECT * FROM design_paradigm WHERE paradigm = '" + name + "'");
    if (set.next()) {
      return new DesignParadigm(set.getInt(1), set.getString(2), set.getString(3));
    } else {
      return null;
    }
  }
}
