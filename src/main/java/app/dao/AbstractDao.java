package app.dao;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class AbstractDao {
  private static final Logger logger = LogManager.getLogger(AbstractDao.class);

  void rollBack(Connection connection) throws SQLException {
    try{
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
