package app.service;

import app.model.Textbook;

import java.sql.SQLException;
import java.util.List;

public interface TextbookService {

  List<Textbook> getTextbooks() throws SQLException;
}
