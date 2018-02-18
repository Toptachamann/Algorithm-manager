package app.auxiliary;

import org.hibernate.engine.jdbc.internal.BasicFormatterImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Util {
  private static final BasicFormatterImpl formatter = new BasicFormatterImpl();


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

  public static String format(String sql){
    return formatter.format(sql);
  }

  public static String format(PreparedStatement statement){
    String str = statement.toString();
    return formatter.format(str.substring(str.indexOf(":") + 1, str.length()));
  }
}
