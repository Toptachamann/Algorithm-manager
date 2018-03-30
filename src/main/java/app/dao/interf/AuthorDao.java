package app.dao.interf;

import app.model.Author;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface AuthorDao {
  Optional<Author> getAuthorByName(String firstName, String lastName) throws Exception;

  default boolean containsAuthor(String firstName, String lastName) throws Exception{
    return getAuthorByName(firstName, lastName).isPresent();
  }

  Author createAuthor(String firstName, String lastName) throws Exception;
}
