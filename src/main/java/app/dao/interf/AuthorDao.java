package app.dao.interf;

import app.model.Author;

import java.util.Optional;

public interface AuthorDao {
  Optional<Author> getAuthor(String firstName, String lastName) throws Exception;

  default boolean containsAuthor(String firstName, String lastName) throws Exception{
    return getAuthor(firstName, lastName).isPresent();
  }

  Author createAuthor(String firstName, String lastName) throws Exception;

  void deleteAuthor(Author author) throws Exception;
}
