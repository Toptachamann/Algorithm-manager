package app.dao;

import app.model.Author;

import java.sql.SQLException;

public interface AuthorDao {
  Author insertAuthor(String firstName, String lastName) throws SQLException;
}
