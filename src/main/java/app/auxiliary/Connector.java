package app.auxiliary;

import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Connector {
  private static Connection connection;

  private Connector() {}

  @NotNull
  public static Connection getConnection() throws SQLException {
    if (connection == null) {
      try {
        Class.forName("com.mysql.jdbc.Driver");
      } catch (ClassNotFoundException e) {
        e.printStackTrace();
        throw new RuntimeException("Booom");
      }
      connection =
          DriverManager.getConnection(
              "jdbc:mysql://localhost/algorithms?" + "user=timofey&password=pass");
    }
    return connection;
  }
}
