package app.dao;

import app.model.Author;
import app.model.Book;
import org.jetbrains.annotations.Nullable;

import java.sql.SQLException;
import java.util.List;

public interface TextbookDao {
  Book insertTextbook(
      String title,
      @Nullable Integer volume,
      Integer edition,
      List<Author> authors)
      throws SQLException;

  List<Book> getTextbooks() throws SQLException;

  List<Book> searchTextbook(String title, Integer volume, Integer edition, List<Author> authors)
      throws SQLException;

  <T> void updateBook(String column, T value, int id) throws SQLException;

  void deleteTextbookById(int id) throws SQLException;

  void addAuthors(int bookId, List<Author> authors) throws SQLException;

  void setAuthors(int bookId, List<Author> authors) throws SQLException;

  List<Book> getTextbooksByAlgorithm(int algorithmId) throws SQLException;
}
