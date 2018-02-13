package app.dao;

import app.model.DesignParadigm;

import java.sql.SQLException;
import java.util.List;

public interface ParadigmDao {
  void insertParadigm(String paradigm) throws SQLException;

  void insertParadigm(String paradigm, String description) throws SQLException;

  List<DesignParadigm> getAllDesignParadigms() throws SQLException;

  DesignParadigm getParadigmByName(String name) throws SQLException;
}
