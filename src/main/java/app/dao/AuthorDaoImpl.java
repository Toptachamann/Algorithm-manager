package app.dao;

import app.auxiliary.Connector;
import app.auxiliary.Util;
import app.model.Author;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AuthorDaoImpl extends AbstractDao implements AuthorDao {
  private static final Logger logger = LogManager.getLogger(AuthorDaoImpl.class);

  private Connection connection;

  private PreparedStatement getAuthorByName;
  private PreparedStatement getAuthorId;
  private PreparedStatement insertAuthor;

  public AuthorDaoImpl() throws SQLException {
    connection = Connector.getConnection();
    insertAuthor =
        connection.prepareStatement("INSERT INTO author (first_name, last_name) VALUE (?, ?)");
    getAuthorByName =
        connection.prepareStatement("SELECT * FROM author WHERE first_name = ? && last_name = ?");
    getAuthorId =
        connection.prepareStatement(
            "SELECT author_id from author WHERE first_name = ? AND last_name = ? LIMIT 1");
  }

  @Override
  public List<Author> getAuthorByName(String firstName, String lastName) throws SQLException {
    List<Author> authors = new ArrayList<>();
    getAuthorByName.setString(1, firstName);
    getAuthorByName.setString(2, lastName);
    logger.debug(() -> Util.format(getAuthorByName));
    ResultSet set = getAuthorByName.executeQuery();
    while (set.next()) {
      authors.add(new Author(set.getInt(1), set.getString(2), set.getString(3)));
    }
    return authors;
  }

  @Override
  public int getAuthorId(String firstName, String lastName) throws SQLException {
    getAuthorId.setString(1, firstName);
    getAuthorId.setString(2, lastName);
    logger.debug(() -> Util.format(getAuthorId));
    ResultSet set = getAuthorId.executeQuery();
    if (set.next()) {
      return set.getInt(1);
    } else {
      return -1;
    }
  }

  @Override
  public Author createAuthor(String firstName, String lastName) throws SQLException {
    int id = getAuthorId(firstName, lastName);
    if (id != -1) {
      return new Author(id, firstName, lastName);
    } else {
      try {
        insertAuthor.setString(1, firstName);
        insertAuthor.setString(2, lastName);
        logger.debug(() -> Util.format(insertAuthor));
        insertAuthor.executeUpdate();
        connection.commit();
        return new Author(getLastId(connection), firstName, lastName);
      } catch (SQLException e) {
        logger.catching(Level.ERROR, e);
        logger.error(
            "Failed to create author with first name {} and last name {}", firstName, lastName);
        rollBack(connection);
        throw e;
      }
    }
  }
}
