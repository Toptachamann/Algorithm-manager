package app.dao;

import app.model.Author;
import app.model.Textbook;
import org.jetbrains.annotations.Nullable;

import java.sql.SQLException;
import java.util.List;

public interface TextbookDao {
  Textbook insertTextbook(
      String title, @Nullable Integer volume, Integer edition, List<Author> authors)
      throws SQLException;

  List<Textbook> getTextbooks() throws SQLException;

  List<Textbook> searchTextbook(String title, Integer volume,  Integer edition, List<Author> authors)
      throws SQLException;

  <T> void updateBook(String column, T value, int id) throws SQLException;

  void deleteTextbookById(int id) throws SQLException;

  void setAuthors(Textbook textbook, List<Author> authors) throws SQLException;
}
