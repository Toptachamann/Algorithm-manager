package app.dao;

import app.model.Author;

import java.sql.SQLException;
import java.util.List;

public interface AuthorDao {
  List<Author> getAuthorByName(String firstName, String lastName) throws SQLException;

  Author insertAuthor(String firstName, String lastName) throws SQLException;
}
