package app.dao;

import app.auxiliary.Connector;
import app.auxiliary.Util;
import app.model.Author;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AuthorDaoImpl implements AuthorDao {
  private Connection connection;

  private PreparedStatement insertAuthor;

  public AuthorDaoImpl() throws SQLException {
    connection = Connector.getConnection();

    insertAuthor =
        connection.prepareStatement("INSERT INTO author (first_name, last_name) VALUE (?, ?)");
  }

  @Override
  public Author insertAuthor(String firstName, String lastName) throws SQLException {
    // TODO add transaction
    insertAuthor.setString(1, firstName);
    insertAuthor.setString(2, lastName);
    insertAuthor.executeUpdate();
    return new Author(Util.getLastId(connection), firstName, lastName);
  }
}
