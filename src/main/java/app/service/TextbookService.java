package app.service;

import app.auxiliary.LogicException;
import app.model.Algorithm;
import app.model.Author;
import app.model.Textbook;

import java.sql.SQLException;
import java.util.List;

public interface TextbookService {

  Textbook createTextbook(String title, Integer volumeStr, Integer editionStr, List<Author> authors)
  throws SQLException;

  List<Textbook> getAllTextbooks() throws SQLException;

  List<Textbook> searchTextbooks(String title, Integer editionStr, Integer volumeStr, List<Author> authors)
  throws SQLException;

  void deleteTextbook(Textbook textbook) throws SQLException;

  Author createAuthor(String firstName, String lastName) throws SQLException;

  void updateTitle(Textbook textbook, String title) throws SQLException;

  void updateVolume(Textbook textbook, Integer volume) throws SQLException;

  void updateEdition(Textbook textbook, Integer edition) throws SQLException;

  List<Author> setAuthors(Textbook textbook, List<Author> authors) throws SQLException;

  void createReference(Algorithm algorithm, Textbook textbook) throws SQLException, LogicException;

  List<Textbook> getReferences(Algorithm algorithm) throws SQLException;

  void deleteReference(Algorithm algorithm, Textbook textbook) throws SQLException, LogicException;
}
