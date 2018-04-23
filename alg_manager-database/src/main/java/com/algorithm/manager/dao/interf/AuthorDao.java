package com.algorithm.manager.dao.interf;

import com.algorithm.manager.model.Author;

import java.util.Optional;

public interface AuthorDao {
  int persist(Author author) throws Exception;

  default Author persist(String firstName, String lastName) throws Exception {
    Author author = new Author(firstName, lastName);
    persist(author);
    return author;
  }

  void refresh(Author author) throws Exception;

  Optional<Author> getByFullName(String firstName, String lastName) throws Exception;

  Optional<Author> getById(int id) throws Exception;

  default boolean containsAuthorWithId(int id) throws Exception {
    return getById(id).isPresent();
  }

  default boolean containsAuthorWithFullName(String firstName, String lastName) throws Exception {
    return getByFullName(firstName, lastName).isPresent();
  }

  void merge(Author author) throws Exception;

  void setFullName(int id, String firstName, String lastName) throws Exception;

  default void setFullName(Author author, String firstName, String lastName) throws Exception {
    setFullName(author.getId(), firstName, lastName);
    author.setFirstName(firstName);
    author.setLastName(lastName);
  }

  void delete(Author author) throws Exception;

  int deleteById(int id) throws Exception;

  int deleteByFullName(String firstName, String lastName) throws Exception;
}
