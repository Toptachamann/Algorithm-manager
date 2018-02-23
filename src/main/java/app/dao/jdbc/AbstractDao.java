package app.dao.jdbc;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class AbstractDao {
  private static final Logger logger = LogManager.getLogger(AbstractDao.class);

  protected static Connection connection;

  static {
    try {
      Class.forName("com.mysql.jdbc.Driver");
      connection =
          DriverManager.getConnection(
              "jdbc:mysql://localhost/algorithms?" + "user=timofey&password=pass");
      connection.setAutoCommit(false);
    } catch (ClassNotFoundException e) {
      logger.error("JDBC driver is not connected");
      logger.catching(Level.ERROR, e);
    } catch (SQLException e) {
      logger.error("Can't obtain database connection");
      logger.catching(Level.ERROR, e);
    }
  }

  public AbstractDao() {
  }

  void rollBack(Connection connection) throws SQLException {
    try {
      logger.info("Rolling changes back");
      connection.rollback();
    } catch (SQLException e) {
      logger.error("Can't roll changes back", e);
      throw e;
    }
  }

  int getLastId(Connection connection) throws SQLException {
    ResultSet set = connection.createStatement().executeQuery("SELECT LAST_INSERT_ID()");
    set.next();
    return set.getInt(1);
  }
}
