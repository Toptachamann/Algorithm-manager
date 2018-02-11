package app.service;

import app.model.Textbook;

import java.sql.SQLException;
import java.util.List;

public interface TextbookService {

  Textbook insertTextbook(String title, Integer edition, Integer volume, String authors) throws SQLException;

  List<Textbook> getTextbooks() throws SQLException;

  List<Textbook> searchTextbook(String title, Integer edition, Integer volume, String authors) throws SQLException;
}
