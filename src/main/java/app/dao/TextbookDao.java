package app.dao;

import app.model.Algorithm;
import app.model.Author;
import app.model.Textbook;
import org.jetbrains.annotations.Nullable;

import java.sql.SQLException;
import java.util.List;

public interface TextbookDao {
  Textbook insertTextbook(
      String title,
      @Nullable Integer volume,
      Integer edition,
      List<Author> authors)
      throws SQLException;

  List<Textbook> getTextbooks() throws SQLException;

  List<Textbook> searchTextbook(String title, Integer volume, Integer edition, List<Author> authors)
      throws SQLException;

  <T> void updateBook(String column, T value, int id) throws SQLException;

  void deleteTextbookById(int id) throws SQLException;

  void addAuthors(int bookId, List<Author> authors) throws SQLException;

  void setAuthors(int bookId, List<Author> authors) throws SQLException;

  boolean containsReference(int algorithmId, int bookId) throws SQLException;

  void createReference(int algorithmId, int bookId) throws SQLException;

  void deleteReference(int algorithmId, int bookId) throws SQLException;

  List<Textbook> getTextbooksByAlgorithm(int algorithmId) throws SQLException;
}
