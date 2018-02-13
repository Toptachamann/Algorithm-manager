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

  List<Textbook> searchTextbook(String title, Integer edition, Integer volume, String authors)
      throws SQLException;

  void updateTitle(String column, String value, int id) throws SQLException;

  void deleteTextbookById(int id) throws SQLException;
}
