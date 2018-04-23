package com.algorithm.manager.dao.interf;

import com.algorithm.manager.model.Author;

import java.util.Optional;

public interface AuthorDao {
  int persist(Author author) throws Exception;

  Optional<Author> getByFullName(String firstName, String lastName) throws Exception;

  Optional<Author> getById(int id) throws Exception;

  default boolean containsAuthorWithId(int id) throws Exception {
    return getById(id).isPresent();
  }

  default boolean containsAuthorWithFullName(String firstName, String lastName) throws Exception {
    return getByFullName(firstName, lastName).isPresent();
  }

  void merge(Author author) throws Exception;

  void delete(Author author) throws Exception;

  void deleteById(int id) throws Exception;

  void deleteByFullName(String firstName, String lastName) throws Exception;
}
