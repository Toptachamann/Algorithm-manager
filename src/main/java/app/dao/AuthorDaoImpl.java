package app.dao;

import app.auxiliary.Connector;
import app.auxiliary.Util;
import app.model.Author;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AuthorDaoImpl implements AuthorDao {
  private static final Logger logger = LogManager.getLogger(AuthorDaoImpl.class);

  private Connection connection;

  private PreparedStatement getAuthorByName;
  private PreparedStatement insertAuthor;

  public AuthorDaoImpl() throws SQLException {
    connection = Connector.getConnection();
    insertAuthor =
        connection.prepareStatement("INSERT INTO author (first_name, last_name) VALUE (?, ?)");
    getAuthorByName =
        connection.prepareStatement("SELECT * FROM author WHERE first_name = ? && last_name = ?");
  }

  @Override
  public List<Author> getAuthorByName(String firstName, String lastName) throws SQLException {
    List<Author> authors = new ArrayList<>();
    getAuthorByName.setString(1, firstName);
    getAuthorByName.setString(2, lastName);
    logger.debug(Util.format(getAuthorByName));
    ResultSet set = getAuthorByName.executeQuery();
    while (set.next()) {
      authors.add(new Author(set.getInt(1), set.getString(2), set.getString(3)));
    }
    return authors;
  }

  @Override
  public Author insertAuthor(String firstName, String lastName) throws SQLException {
    // TODO add transaction
    List<Author> matchedAuthors = getAuthorByName(firstName, lastName);
    if(matchedAuthors.size() > 0){
      return matchedAuthors.get(0);
    }else{
      insertAuthor.setString(1, firstName);
      insertAuthor.setString(2, lastName);
      logger.debug(Util.format(insertAuthor));
      insertAuthor.executeUpdate();
      return new Author(Util.getLastId(connection), firstName, lastName);
    }
  }
}
