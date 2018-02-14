package app.service;

import app.model.Textbook;

import java.sql.SQLException;
import java.util.List;

public interface TextbookService {

  List<Textbook> searchTextbooks(String title, String editionStr, String volumeStr, String authors)
      throws SQLException;

  List<Textbook> getAllTextbooks() throws SQLException;

  void deleteTextbook(Textbook textbook) throws SQLException;

  Textbook createTextbook(String title, String volumeStr, String editionStr, String authors)
      throws SQLException;
}
