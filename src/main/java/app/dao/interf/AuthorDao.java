package app.dao.interf;

import app.model.Author;

import java.sql.SQLException;
import java.util.List;

public interface AuthorDao {
  List<Author> getAuthorByName(String firstName, String lastName) throws SQLException;

  int getAuthorId(String firstName, String lastName) throws SQLException;

  Author createAuthor(String firstName, String lastName) throws SQLException;
}
