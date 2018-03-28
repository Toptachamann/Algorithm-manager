package app.dao.interf;

import app.model.Author;
import app.model.Book;
import org.jetbrains.annotations.Nullable;

import java.sql.SQLException;
import java.util.List;

public interface BookDao {
  Book createBook(
      String title,
      @Nullable Integer volume,
      Integer edition,
      List<Author> authors)
      throws SQLException;

  List<Book> getBooks() throws Exception;

  List<Book> searchBooks(String title, Integer volume, Integer edition, List<Author> authors)
      throws Exception;

  <T> void updateBook(String column, T value, int id) throws Exception;

  void deleteBookById(int id) throws Exception;

  void addAuthors(int bookId, List<Author> authors) throws Exception;

  void setAuthors(int bookId, List<Author> authors) throws Exception;

  List<Book> getBooksByAlgorithm(int algorithmId) throws Exception;
}
