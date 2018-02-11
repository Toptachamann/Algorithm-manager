package app.auxiliary;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Util {
  public static String fixForString(String str) {
    str = str.replace("\\", "\\\\");
    str = str.replace("'", "\\'");
    return "'" + str + "'";
  }

  public static String fixForLike(String str) {
    str = str.replace("\\", "\\\\");
    str = str.replace("'", "\\'");
    str = str.replace("%", "\\%");
    str = str.replace("_", "\\_");
    return "'%" + str + "%'";
  }

  public static int getLastId(Connection connection) throws SQLException {
    ResultSet set = connection.createStatement().executeQuery("SELECT LAST_INSERT_ID()");
    set.next();
    return set.getInt(1);
  }
}
