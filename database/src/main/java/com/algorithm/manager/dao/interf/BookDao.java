package com.algorithm.manager.dao.interf;

import com.algorithm.manager.model.Author;
import com.algorithm.manager.model.Book;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public interface BookDao {
  int persist(Book book) throws Exception;

  List<Book> getBooks() throws Exception;

  Optional<Book> getBookById(int id) throws Exception;

  List<Book> searchBooks(
      @Nullable String title,
      @Nullable Integer volume,
      @Nullable Integer edition,
      @Nullable List<Author> authors)
      throws Exception;

  default boolean containsBookWithId(int id) throws Exception {
    return getBookById(id).isPresent();
  }

  default boolean containsBookWithTitle(String title) throws Exception {
    return title != null && !searchBooks(title, null, null, null).isEmpty();
  }

  void merge(Book book) throws Exception;

  void delete(Book book) throws Exception;

  void deleteById(int id) throws Exception;
}
