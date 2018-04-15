package com.algorithm.manager.dao.interf;

import com.algorithm.manager.model.Author;
import com.algorithm.manager.model.Book;

import java.util.List;
import java.util.Optional;

public interface BookDao {
  void persist(Book book) throws Exception;

  List<Book> getBooks() throws Exception;

  Optional<Book> getBookById(int id) throws Exception;

  List<Book> searchBooks(String title, Integer volume, Integer edition, List<Author> authors)
      throws Exception;

  default boolean containsBook(Book book) throws Exception {
    return getBookById(book.getId()).isPresent();
  }

  void merge(Book book) throws Exception;

  void delete(Book book) throws Exception;
}
