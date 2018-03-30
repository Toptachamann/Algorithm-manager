package app.dao.interf;

import app.model.Author;

import java.sql.SQLException;
import java.util.List;

public interface AuthorDao {
  List<Author> getAuthorByName(String firstName, String lastName) throws Exception;

  default boolean containsAuthor(String firstName, String lastName) throws Exception{
    return !getAuthorByName(firstName, lastName).isEmpty();
  }

  Author createAuthor(String firstName, String lastName) throws Exception;
}
