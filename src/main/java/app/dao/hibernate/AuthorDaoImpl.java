package app.dao.hibernate;

import app.dao.interf.AuthorDao;
import app.model.Author;

import java.sql.SQLException;
import java.util.List;

public class AuthorDaoImpl  extends AbstractDao implements AuthorDao {
  @Override
  public List<Author> getAuthorByName(String firstName, String lastName) throws SQLException {
    return null;
  }

  @Override
  public int getAuthorId(String firstName, String lastName) throws SQLException {
    return 0;
  }

  @Override
  public Author createAuthor(String firstName, String lastName) throws SQLException {
    return null;
  }
}
