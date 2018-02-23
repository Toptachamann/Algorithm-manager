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

  List<Book> getBooks() throws SQLException;

  List<Book> searchBook(String title, Integer volume, Integer edition, List<Author> authors)
      throws SQLException;

  <T> void updateBook(String column, T value, int id) throws SQLException;

  void deleteBookById(int id) throws SQLException;

  void addAuthors(int bookId, List<Author> authors) throws SQLException;

  void setAuthors(int bookId, List<Author> authors) throws SQLException;

  List<Book> getBooksByAlgorithm(int algorithmId) throws SQLException;
}
