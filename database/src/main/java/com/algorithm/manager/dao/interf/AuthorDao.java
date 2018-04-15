package com.algorithm.manager.dao.interf;

import com.algorithm.manager.model.Author;

import java.util.Optional;

public interface AuthorDao {
  void persist(Author author) throws Exception;

  Optional<Author> getAuthor(String firstName, String lastName) throws Exception;

  default boolean containsAuthor(String firstName, String lastName) throws Exception {
    return getAuthor(firstName, lastName).isPresent();
  }

  void delete(Author author) throws Exception;
}
