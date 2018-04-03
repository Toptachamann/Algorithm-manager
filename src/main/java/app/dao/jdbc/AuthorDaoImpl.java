package app.dao.jdbc;

import app.auxiliary.Util;
import app.dao.interf.AuthorDao;
import app.model.Author;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class AuthorDaoImpl extends AbstractDao implements AuthorDao {
  private static final Logger logger = LogManager.getLogger(AuthorDaoImpl.class);

  private PreparedStatement getAuthorByName;
  private PreparedStatement insertAuthor;
  private PreparedStatement deleteAuthor;

  public AuthorDaoImpl() throws SQLException {
    insertAuthor =
        connection.prepareStatement("INSERT INTO author (first_name, last_name) VALUE (?, ?)");
    getAuthorByName =
        connection.prepareStatement("SELECT * FROM author WHERE first_name = ? && last_name = ?");
    deleteAuthor = connection.prepareStatement("DELETE FROM author WHERE author_id = ?");
  }

  @Override
  public Optional<Author> getAuthor(String firstName, String lastName) throws SQLException {
    getAuthorByName.setString(1, firstName);
    getAuthorByName.setString(2, lastName);
    logger.debug(() -> Util.format(getAuthorByName));
    ResultSet set = getAuthorByName.executeQuery();
    return set.next()
        ? Optional.of(new Author(set.getInt(1), set.getString(2), set.getString(3)))
        : Optional.empty();
  }

  @Override
  public void persist(Author author) throws SQLException {
    try {
      insertAuthor.setString(1, author.getFirstName());
      insertAuthor.setString(2, author.getLastName());
      logger.debug(() -> Util.format(insertAuthor));
      insertAuthor.executeUpdate();
      connection.commit();
      author.setId(getLastId(connection));
    } catch (SQLException e) {
      logger.catching(Level.ERROR, e);
      logger.error("Failed to persist author {}", author);
      rollBack(connection);
      throw e;
    }
  }

  @Override
  public void deleteAuthor(Author author) throws Exception {
    try {
      deleteAuthor.setInt(1, author.getId());
      logger.debug(() -> Util.format(deleteAuthor));
      deleteAuthor.executeUpdate();
      connection.commit();
    } catch (SQLException e) {
      logger.catching(Level.ERROR, e);
      logger.error("Failed to delete author {}", author);
      rollBack(connection);
      throw e;
    }
  }
}
