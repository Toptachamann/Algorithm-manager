package app.dao.interf;

import app.model.Author;
import app.model.Book;

import java.util.List;
import java.util.Optional;

public interface BookDao {
  void persist(Book book) throws Exception;

  List<Book> getBooks() throws Exception;

  List<Book> searchBooks(String title, Integer volume, Integer edition, List<Author> authors)
      throws Exception;

  Optional<Book> getBookById(int id) throws Exception;

  default boolean containsBook(Book book) throws Exception {
    return getBookById(book.getId()).isPresent();
  }

  void merge(Book book) throws Exception;

  void deleteBook(Book book) throws Exception;
}
