package app.service;

import app.auxiliary.LogicException;
import app.model.Algorithm;
import app.model.Author;
import app.model.Book;

import java.sql.SQLException;
import java.util.List;

public interface TextbookService {

  Book createTextbook(String title, Integer volumeStr, Integer editionStr, List<Author> authors)
  throws SQLException;

  List<Book> getAllTextbooks() throws SQLException;

  List<Book> searchTextbooks(String title, Integer editionStr, Integer volumeStr, List<Author> authors)
  throws SQLException;

  void deleteTextbook(Book book) throws SQLException;

  Author createAuthor(String firstName, String lastName) throws SQLException;

  void updateTitle(Book book, String title) throws SQLException;

  void updateVolume(Book book, Integer volume) throws SQLException;

  void updateEdition(Book book, Integer edition) throws SQLException;

  List<Author> setAuthors(Book book, List<Author> authors) throws SQLException;

  void createReference(Algorithm algorithm, Book book) throws SQLException, LogicException;

  List<Book> getReferences(Algorithm algorithm) throws SQLException;

  void deleteReference(Algorithm algorithm, Book book) throws SQLException, LogicException;
}
